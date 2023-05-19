package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.BundleBase
import com.intellij.application.options.ModulesComboBox
import com.intellij.icons.AllIcons
import com.intellij.ide.util.ClassFilter
import com.intellij.ide.util.TreeClassChooserFactory
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.actionSystem.impl.ActionButton
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.module.StdModuleTypes
import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.ui.panel.ComponentPanelBuilder
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.JavaCodeFragment
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.ui.*
import com.intellij.ui.components.*
import com.intellij.ui.components.fields.ExpandableTextField
import com.intellij.ui.layout.*
import com.intellij.util.Function
import com.intellij.util.execution.ParametersListUtil
import com.intellij.util.ui.JBFont
import com.intellij.util.ui.StatusText
import com.intellij.util.ui.UIUtil
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls
import java.awt.Component
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.ItemEvent
import java.awt.event.MouseEvent
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.text.JTextComponent
import kotlin.jvm.internal.CallableReference
import kotlin.reflect.KMutableProperty0

@DslMarker
annotation class ConceptCellMarker

data class ConceptPropertyBinding<V>(val get: () -> V, val set: (V) -> Unit)

@PublishedApi
internal fun <T> createPropertyBinding(prop: KMutableProperty0<T>, propType: Class<T>): ConceptPropertyBinding<T> {
    if (prop is CallableReference) {
        val name = prop.name
        val receiver = (prop as CallableReference).boundReceiver
        if (receiver != null) {
            val baseName = name.removePrefix("is")
            val nameCapitalized = StringUtil.capitalize(baseName)
            val getterName = if (name.startsWith("is")) name else "get$nameCapitalized"
            val setterName = "set$nameCapitalized"
            val receiverClass = receiver::class.java

            try {
                val getter = receiverClass.getMethod(getterName)
                val setter = receiverClass.getMethod(setterName, propType)
                return ConceptPropertyBinding({ getter.invoke(receiver) as T }, { setter.invoke(receiver, it) })
            } catch (e: Exception) {
                // ignore
            }

            try {
                val field = receiverClass.getDeclaredField(name)
                field.isAccessible = true
                return ConceptPropertyBinding({ field.get(receiver) as T }, { field.set(receiver, it) })
            } catch (e: Exception) {
                // ignore
            }
        }
    }
    return ConceptPropertyBinding(prop.getter, prop.setter)
}

fun <T> ConceptPropertyBinding<T>.toNullable(): ConceptPropertyBinding<T?> {
    return ConceptPropertyBinding<T?>({ get() }, { set(it!!) })
}

inline fun <reified T : Any> KMutableProperty0<T>.toBinding(): ConceptPropertyBinding<T> {
    return createPropertyBinding(this, T::class.javaPrimitiveType ?: T::class.java)
}

inline fun <reified T : Any> KMutableProperty0<T?>.toNullableBinding(defaultValue: T): ConceptPropertyBinding<T> {
    return ConceptPropertyBinding({ get() ?: defaultValue }, { set(it) })
}

class ConceptValidationInfoBuilder(val component: JComponent) {
    fun error(@NlsContexts.DialogMessage message: String): ValidationInfo = ValidationInfo(message, component)
    fun warning(@NlsContexts.DialogMessage message: String): ValidationInfo =
        ValidationInfo(message, component).asWarning().withOKEnabled()
}

interface ConceptCellBuilder<out T : JComponent> {
    val component: T

    fun comment(
        @NlsContexts.DetailedDescription text: String,
        maxLineLength: Int = 70,
        forComponent: Boolean = false
    ): ConceptCellBuilder<T>

    fun commentComponent(component: JComponent, forComponent: Boolean = false): ConceptCellBuilder<T>
    fun focused(): ConceptCellBuilder<T>
    fun withValidationOnApply(callback: ConceptValidationInfoBuilder.(T) -> ValidationInfo?): ConceptCellBuilder<T>
    fun withValidationOnInput(callback: ConceptValidationInfoBuilder.(T) -> ValidationInfo?): ConceptCellBuilder<T>
    fun onApply(callback: () -> Unit): ConceptCellBuilder<T>
    fun onReset(callback: () -> Unit): ConceptCellBuilder<T>
    fun onIsModified(callback: () -> Boolean): ConceptCellBuilder<T>

    /**
     * All components of the same group share will get the same BoundSize (min/preferred/max),
     * which is that of the biggest component in the group
     */
    fun sizeGroup(name: String): ConceptCellBuilder<T>
    fun growPolicy(growPolicy: ConceptGrowPolicy): ConceptCellBuilder<T>
    fun constraints(vararg constraints: CCFlags): ConceptCellBuilder<T>

    /**
     * If this method is called, the value of the component will be stored to the backing property only if the component is enabled.
     */
    fun applyIfEnabled(): ConceptCellBuilder<T>

    fun <V> withBinding(
        componentGet: (T) -> V,
        componentSet: (T, V) -> Unit,
        modelBinding: ConceptPropertyBinding<V>
    ): ConceptCellBuilder<T> {
        onApply { if (shouldSaveOnApply()) modelBinding.set(componentGet(component)) }
        onReset { componentSet(component, modelBinding.get()) }
        onIsModified { shouldSaveOnApply() && componentGet(component) != modelBinding.get() }
        return this
    }

    fun withGraphProperty(property: GraphProperty<*>): ConceptCellBuilder<T>

    fun enabled(isEnabled: Boolean)
    fun enableIf(predicate: ComponentPredicate): ConceptCellBuilder<T>

    fun visible(isVisible: Boolean)
    fun visibleIf(predicate: ComponentPredicate): ConceptCellBuilder<T>

    fun withErrorOnApplyIf(
        @NlsContexts.DialogMessage message: String,
        callback: (T) -> Boolean
    ): ConceptCellBuilder<T> {
        withValidationOnApply { if (callback(it)) error(message) else null }
        return this
    }

    @ApiStatus.Internal
    fun shouldSaveOnApply(): Boolean

    fun withLargeLeftGap(): ConceptCellBuilder<T>

    fun withLeftGap(): ConceptCellBuilder<T>

    fun withLeftGap(gapLeft: Int): ConceptCellBuilder<T>
}

internal interface ConceptCheckboxCellBuilder {
    fun actsAsLabel()
}

fun <T : JCheckBox> ConceptCellBuilder<T>.actsAsLabel(): ConceptCellBuilder<T> {
    (this as ConceptCheckboxCellBuilder).actsAsLabel()
    return this
}

fun <T : JComponent> ConceptCellBuilder<T>.applyToComponent(task: T.() -> Unit): ConceptCellBuilder<T> {
    return also { task(component) }
}

internal interface ConceptScrollPaneCellBuilder {
    fun noGrowY()
}

fun <T : JScrollPane> ConceptCellBuilder<T>.noGrowY(): ConceptCellBuilder<T> {
    (this as ConceptScrollPaneCellBuilder).noGrowY()
    return this
}

fun <T : JTextComponent> ConceptCellBuilder<T>.withTextBinding(modelBinding: ConceptPropertyBinding<String>): ConceptCellBuilder<T> {
    return withBinding(JTextComponent::getText, JTextComponent::setText, modelBinding)
}

fun <T : AbstractButton> ConceptCellBuilder<T>.withSelectedBinding(modelBinding: ConceptPropertyBinding<Boolean>): ConceptCellBuilder<T> {
    return withBinding(AbstractButton::isSelected, AbstractButton::setSelected, modelBinding)
}

fun <T : ModulesComboBox> ConceptCellBuilder<T>.withModulesComboBoxBinding(modelBinding: ConceptPropertyBinding<Module?>): ConceptCellBuilder<T> {
    return withBinding(ModulesComboBox::getSelectedModule, ModulesComboBox::setSelectedModule, modelBinding)
}

fun <T : ReferenceEditorComboWithBrowseButton> ConceptCellBuilder<T>.withClassesComboBoxBinding(modelBinding: ConceptPropertyBinding<String>): ConceptCellBuilder<T> {
    return withBinding(
        ReferenceEditorComboWithBrowseButton::getText,
        ReferenceEditorComboWithBrowseButton::setText,
        modelBinding
    )
}

val ConceptCellBuilder<AbstractButton>.selected
    get() = component.selected

const val UNBOUND_RADIO_BUTTON = "unbound.radio.button"

// separate class to avoid row related methods in the `cell { } `
@ConceptCellMarker
abstract class ConceptCell : ConceptBaseBuilder {
    /**
     * Sets how keen the component should be to grow in relation to other component **in the same cell**. Use `push` in addition if need.
     * If this constraint is not set the grow weight is set to 0 and the component will not grow (unless some automatic rule is not applied (see [com.intellij.ui.layout.panel])).
     * Grow weight will only be compared against the weights for the same cell.
     */
    val growX = CCFlags.growX

    @Suppress("unused")
    val growY = CCFlags.growY
    val grow = CCFlags.grow

    /**
     * Makes the column that the component is residing in grow with `weight`.
     */
    val pushX = CCFlags.pushX

    /**
     * Makes the row that the component is residing in grow with `weight`.
     */
    @Suppress("unused")
    val pushY = CCFlags.pushY
    val push = CCFlags.push

    fun label(
        @NlsContexts.Label text: String,
        style: UIUtil.ComponentStyle? = null,
        fontColor: UIUtil.FontColor? = null,
        bold: Boolean = false
    ): ConceptCellBuilder<JLabel> {
        val label = Label(text, style, fontColor, bold)
        return component(label)
    }

    fun label(
        @NlsContexts.Label text: String,
        font: JBFont,
        fontColor: UIUtil.FontColor? = null
    ): ConceptCellBuilder<JLabel> {
        val label = Label(text, fontColor = fontColor, font = font)
        return component(label)
    }

    fun link(
        @NlsContexts.LinkLabel text: String,
        style: UIUtil.ComponentStyle? = null,
        action: () -> Unit
    ): ConceptCellBuilder<JComponent> {
        val result = Link(text, style, action)
        return component(result)
    }

    fun browserLink(@NlsContexts.LinkLabel text: String, url: String): ConceptCellBuilder<JComponent> {
        val result = BrowserLink(text, url)
        return component(result)
    }

    fun buttonFromAction(
        @NlsContexts.Button text: String,
        @NonNls actionPlace: String,
        action: AnAction
    ): ConceptCellBuilder<JButton> {
        val button = JButton(BundleBase.replaceMnemonicAmpersand(text))
        button.addActionListener { ActionUtil.invokeAction(action, button, actionPlace, null, null) }
        return component(button)
    }

    fun button(
        @NlsContexts.Button text: String,
        actionListener: (event: ActionEvent) -> Unit
    ): ConceptCellBuilder<JButton> {
        val button = JButton(BundleBase.replaceMnemonicAmpersand(text))
        button.addActionListener(actionListener)
        return component(button)
    }

    inline fun checkBox(
        @NlsContexts.Checkbox text: String,
        isSelected: Boolean = false,
        @NlsContexts.DetailedDescription comment: String? = null,
        crossinline actionListener: (event: ActionEvent, component: JCheckBox) -> Unit
    ): ConceptCellBuilder<JBCheckBox> {
        return checkBox(text, isSelected, comment)
            .applyToComponent {
                addActionListener(ActionListener { actionListener(it, this) })
            }
    }

    @JvmOverloads
    fun checkBox(
        @NlsContexts.Checkbox text: String,
        isSelected: Boolean = false,
        @NlsContexts.DetailedDescription comment: String? = null
    ): ConceptCellBuilder<JBCheckBox> {
        val result = JBCheckBox(text, isSelected)
        return result(comment = comment)
    }

    fun checkBox(
        @NlsContexts.Checkbox text: String,
        prop: KMutableProperty0<Boolean>,
        @NlsContexts.DetailedDescription comment: String? = null
    ): ConceptCellBuilder<JBCheckBox> {
        return checkBox(text, prop.toBinding(), comment)
    }

    fun checkBox(
        @NlsContexts.Checkbox text: String,
        getter: () -> Boolean,
        setter: (Boolean) -> Unit,
        @NlsContexts.DetailedDescription comment: String? = null
    ): ConceptCellBuilder<JBCheckBox> {
        return checkBox(text, ConceptPropertyBinding(getter, setter), comment)
    }

    private fun checkBox(
        @NlsContexts.Checkbox text: String,
        modelBinding: ConceptPropertyBinding<Boolean>,
        @NlsContexts.DetailedDescription comment: String?
    ): ConceptCellBuilder<JBCheckBox> {
        val component = JBCheckBox(text, modelBinding.get())
        return component(comment = comment).withSelectedBinding(modelBinding)
    }

    fun checkBox(
        @NlsContexts.Checkbox text: String,
        property: GraphProperty<Boolean>,
        @NlsContexts.DetailedDescription comment: String? = null
    ): ConceptCellBuilder<JBCheckBox> {
        val component = JBCheckBox(text, property.get())
        return component(comment = comment).withGraphProperty(property).applyToComponent { component.bind(property) }
    }

    open fun radioButton(
        @NlsContexts.RadioButton text: String,
        @Nls comment: String? = null
    ): ConceptCellBuilder<JBRadioButton> {
        val component = JBRadioButton(text)
        component.putClientProperty(UNBOUND_RADIO_BUTTON, true)
        return component(comment = comment)
    }

    open fun radioButton(
        @NlsContexts.RadioButton text: String,
        getter: () -> Boolean,
        setter: (Boolean) -> Unit,
        @Nls comment: String? = null
    ): ConceptCellBuilder<JBRadioButton> {
        val component = JBRadioButton(text, getter())
        return component(comment = comment).withSelectedBinding(ConceptPropertyBinding(getter, setter))
    }

    open fun radioButton(
        @NlsContexts.RadioButton text: String,
        prop: KMutableProperty0<Boolean>,
        @Nls comment: String? = null
    ): ConceptCellBuilder<JBRadioButton> {
        val component = JBRadioButton(text, prop.get())
        return component(comment = comment).withSelectedBinding(prop.toBinding())
    }

    fun <T> comboBox(
        model: ComboBoxModel<T>,
        getter: () -> T?,
        setter: (T?) -> Unit,
        renderer: ListCellRenderer<T?>? = null
    ): ConceptCellBuilder<ComboBox<T>> {
        return comboBox(model, ConceptPropertyBinding(getter, setter), renderer)
    }

    fun <T> comboBox(
        model: ComboBoxModel<T>,
        modelBinding: ConceptPropertyBinding<T?>,
        renderer: ListCellRenderer<T?>? = null
    ): ConceptCellBuilder<ComboBox<T>> {
        return component(ComboBox(model))
            .applyToComponent {
                this.renderer = renderer ?: SimpleListCellRenderer.create("") { it.toString() }
                selectedItem = modelBinding.get()
            }
            .withBinding(
                { component -> component.selectedItem as T? },
                { component, value -> component.setSelectedItem(value) },
                modelBinding
            )
    }

    inline fun <reified T : Any> comboBox(
        model: ComboBoxModel<T>,
        prop: KMutableProperty0<T>,
        renderer: ListCellRenderer<T?>? = null
    ): ConceptCellBuilder<ComboBox<T>> {
        return comboBox(model, prop.toBinding().toNullable(), renderer)
    }

    fun <T> comboBox(
        model: ComboBoxModel<T>,
        property: GraphProperty<T>,
        renderer: ListCellRenderer<T?>? = null
    ): ConceptCellBuilder<ComboBox<T>> {
        return comboBox(model, ConceptPropertyBinding(property::get, property::set).toNullable(), renderer)
            .withGraphProperty(property)
            .applyToComponent { bind(property) }
    }

    fun modulesComboBox(project: Project, property: GraphProperty<Module?>): ConceptCellBuilder<ModulesComboBox> {
        return modulesComboBox(project, property::get, property::set)
            .withGraphProperty(property)
            .applyToComponent { bind(property) }
    }

    fun modulesComboBox(
        project: Project,
        getter: () -> Module?,
        setter: (Module?) -> Unit
    ): ConceptCellBuilder<ModulesComboBox> {
        return modulesComboBox(project, ConceptPropertyBinding(getter, setter))
    }

    fun modulesComboBox(
        project: Project,
        modelBinding: ConceptPropertyBinding<Module?>
    ): ConceptCellBuilder<ModulesComboBox> {
        return component(ModulesComboBox())
            .applyToComponent {
                val allModules = ModuleUtil.getModulesOfType(project, StdModuleTypes.JAVA)
                val modules: MutableList<Module> = ArrayList()
                for (module in allModules) {
                    if (module.name.endsWith(".main")) {
                        continue
                    }
                    if (module.name.endsWith(".test")) {
                        continue
                    }
                    modules.add(module)
                }
                setModules(modules)
                //fillModules(project, StdModuleTypes.JAVA)
                //this.renderer = renderer ?: SimpleListCellRenderer.create("") { it.toString() }
                selectedModule = modelBinding.get()
            }
            .withBinding(
                { component -> component.selectedModule },
                { component, value -> component.selectedModule = value },
                modelBinding
            )
    }

    fun classesComboBox(
        project: Project,
        recentsKey: String,
        property: GraphProperty<String>
    ): ConceptCellBuilder<ReferenceEditorComboWithBrowseButton> {
        return classesComboBox(project, recentsKey, property::get, property::set)
            .withGraphProperty(property)
            .applyToComponent { bind(property) }
    }

    fun classesComboBox(
        project: Project,
        recentsKey: String,
        getter: () -> String,
        setter: (String) -> Unit
    ): ConceptCellBuilder<ReferenceEditorComboWithBrowseButton> {
        return classesComboBox(project, recentsKey, ConceptPropertyBinding(getter, setter))
    }

    fun classesComboBox(
        project: Project,
        recentsKey: String,
        modelBinding: ConceptPropertyBinding<String>
    ): ConceptCellBuilder<ReferenceEditorComboWithBrowseButton> {
        return component(
            ReferenceEditorComboWithBrowseButton(
                null,
                modelBinding.get(),
                project,
                true,
                JavaCodeFragment.VisibilityChecker.PROJECT_SCOPE_VISIBLE,
                recentsKey
            )
        ).applyToComponent {

            text = modelBinding.get()
            addActionListener(ActionListener {
                val chooser = TreeClassChooserFactory.getInstance(project).createWithInnerClassesScopeChooser(
                    "Choose User Domain Class", GlobalSearchScope.projectScope(project),
                    ClassFilter.ALL, null
                )
                val targetClassName: String? = text
                if (targetClassName != null) {
                    val aClass = JavaPsiFacade.getInstance(project)
                        .findClass(targetClassName, GlobalSearchScope.allScope(project))
                    if (aClass != null) {
                        chooser.selectDirectory(aClass.containingFile.containingDirectory)
                    } /*else {
                            chooser.selectDirectory(mySourceClass.getContainingFile().getContainingDirectory())
                        }*/
                }
                chooser.showDialog()
                val aClass = chooser.selected
                if (aClass != null) {
                    text = aClass.qualifiedName
                }
            })
        }.withBinding(
            { component -> component.text },
            { component, value -> component.text = value },
            modelBinding
        )
    }

    fun textField(prop: KMutableProperty0<String>, columns: Int? = null): ConceptCellBuilder<JBTextField> =
        textField(prop.toBinding(), columns)

    fun textField(getter: () -> String, setter: (String) -> Unit, columns: Int? = null) =
        textField(ConceptPropertyBinding(getter, setter), columns)

    fun textField(binding: ConceptPropertyBinding<String>, columns: Int? = null): ConceptCellBuilder<JBTextField> {
        return component(JBTextField(binding.get(), columns ?: 0))
            .withTextBinding(binding)
    }

    fun textField(property: GraphProperty<String>, columns: Int? = null): ConceptCellBuilder<JBTextField> {
        return textField(property::get, property::set, columns)
            .withGraphProperty(property)
            .applyToComponent { bind(property) }
    }

    fun textArea(
        prop: KMutableProperty0<String>,
        rows: Int? = null,
        columns: Int? = null
    ): ConceptCellBuilder<JBTextArea> = textArea(prop.toBinding(), rows, columns)

    fun textArea(getter: () -> String, setter: (String) -> Unit, rows: Int? = null, columns: Int? = null) =
        textArea(ConceptPropertyBinding(getter, setter), rows, columns)

    fun textArea(
        binding: ConceptPropertyBinding<String>,
        rows: Int? = null,
        columns: Int? = null
    ): ConceptCellBuilder<JBTextArea> {
        return component(JBTextArea(binding.get(), rows ?: 0, columns ?: 0))
            .withTextBinding(binding)
    }

    fun textArea(
        property: GraphProperty<String>,
        rows: Int? = null,
        columns: Int? = null
    ): ConceptCellBuilder<JBTextArea> {
        return textArea(property::get, property::set, rows, columns)
            .withGraphProperty(property)
            .applyToComponent { bind(property) }
    }

    fun scrollableTextArea(prop: KMutableProperty0<String>, rows: Int? = null): ConceptCellBuilder<JBTextArea> =
        scrollableTextArea(prop.toBinding(), rows)

    fun scrollableTextArea(getter: () -> String, setter: (String) -> Unit, rows: Int? = null) =
        scrollableTextArea(ConceptPropertyBinding(getter, setter), rows)

    fun scrollableTextArea(binding: ConceptPropertyBinding<String>, rows: Int? = null): ConceptCellBuilder<JBTextArea> {
        val textArea = JBTextArea(binding.get(), rows ?: 0, 0)
        val scrollPane = JBScrollPane(textArea)
        return component(textArea, scrollPane)
            .withTextBinding(binding)
    }

    fun scrollableTextArea(property: GraphProperty<String>, rows: Int? = null): ConceptCellBuilder<JBTextArea> {
        return scrollableTextArea(property::get, property::set, rows)
            .withGraphProperty(property)
            .applyToComponent { bind(property) }
    }

    fun intTextField(
        prop: KMutableProperty0<Int>,
        columns: Int? = null,
        range: IntRange? = null
    ): ConceptCellBuilder<JBTextField> {
        return intTextField(prop.toBinding(), columns, range)
    }

    fun intTextField(
        getter: () -> Int,
        setter: (Int) -> Unit,
        columns: Int? = null,
        range: IntRange? = null
    ): ConceptCellBuilder<JBTextField> {
        return intTextField(ConceptPropertyBinding(getter, setter), columns, range)
    }

    fun intTextField(
        binding: ConceptPropertyBinding<Int>,
        columns: Int? = null,
        range: IntRange? = null
    ): ConceptCellBuilder<JBTextField> {
        return textField(
            { binding.get().toString() },
            { value ->
                value.toIntOrNull()
                    ?.let { intValue -> binding.set(range?.let { intValue.coerceIn(it.first, it.last) } ?: intValue) }
            },
            columns
        ).withValidationOnInput {
            val value = it.text.toIntOrNull()
            when {
                value == null -> error(UIBundle.message("please.enter.a.number"))
                range != null && value !in range -> error(
                    UIBundle.message(
                        "please.enter.a.number.from.0.to.1",
                        range.first,
                        range.last
                    )
                )

                else -> null
            }
        }
    }

    fun spinner(
        prop: KMutableProperty0<Int>,
        minValue: Int,
        maxValue: Int,
        step: Int = 1
    ): ConceptCellBuilder<JBIntSpinner> {
        val spinner = JBIntSpinner(prop.get(), minValue, maxValue, step)
        return component(spinner).withBinding(JBIntSpinner::getNumber, JBIntSpinner::setNumber, prop.toBinding())
    }

    fun spinner(
        getter: () -> Int,
        setter: (Int) -> Unit,
        minValue: Int,
        maxValue: Int,
        step: Int = 1
    ): ConceptCellBuilder<JBIntSpinner> {
        val spinner = JBIntSpinner(getter(), minValue, maxValue, step)
        return component(spinner).withBinding(
            JBIntSpinner::getNumber,
            JBIntSpinner::setNumber,
            ConceptPropertyBinding(getter, setter)
        )
    }

    fun textFieldWithHistoryWithBrowseButton(
        getter: () -> String,
        setter: (String) -> Unit,
        @NlsContexts.DialogTitle browseDialogTitle: String,
        project: Project? = null,
        fileChooserDescriptor: FileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor(),
        historyProvider: (() -> List<String>)? = null,
        fileChosen: ((chosenFile: VirtualFile) -> String)? = null
    ): ConceptCellBuilder<TextFieldWithHistoryWithBrowseButton> {
        val textField = textFieldWithHistoryWithBrowseButton(
            project,
            browseDialogTitle,
            fileChooserDescriptor,
            historyProvider,
            fileChosen
        )
        val modelBinding = ConceptPropertyBinding(getter, setter)
        textField.text = modelBinding.get()
        return component(textField)
            .withBinding(
                TextFieldWithHistoryWithBrowseButton::getText,
                TextFieldWithHistoryWithBrowseButton::setText,
                modelBinding
            )
    }

    fun textFieldWithBrowseButton(
        @NlsContexts.DialogTitle browseDialogTitle: String? = null,
        value: String? = null,
        project: Project? = null,
        fileChooserDescriptor: FileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor(),
        fileChosen: ((chosenFile: VirtualFile) -> String)? = null
    ): ConceptCellBuilder<TextFieldWithBrowseButton> {
        val textField = textFieldWithBrowseButton(project, browseDialogTitle, fileChooserDescriptor, fileChosen)
        if (value != null) textField.text = value
        return component(textField)
    }

    fun textFieldWithBrowseButton(
        prop: KMutableProperty0<String>,
        @NlsContexts.DialogTitle browseDialogTitle: String? = null,
        project: Project? = null,
        fileChooserDescriptor: FileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor(),
        fileChosen: ((chosenFile: VirtualFile) -> String)? = null
    ): ConceptCellBuilder<TextFieldWithBrowseButton> {
        val modelBinding = prop.toBinding()
        return textFieldWithBrowseButton(modelBinding, browseDialogTitle, project, fileChooserDescriptor, fileChosen)
    }

    fun textFieldWithBrowseButton(
        getter: () -> String,
        setter: (String) -> Unit,
        @NlsContexts.DialogTitle browseDialogTitle: String? = null,
        project: Project? = null,
        fileChooserDescriptor: FileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor(),
        fileChosen: ((chosenFile: VirtualFile) -> String)? = null
    ): ConceptCellBuilder<TextFieldWithBrowseButton> {
        val modelBinding = ConceptPropertyBinding(getter, setter)
        return textFieldWithBrowseButton(modelBinding, browseDialogTitle, project, fileChooserDescriptor, fileChosen)
    }

    fun textFieldWithBrowseButton(
        modelBinding: ConceptPropertyBinding<String>,
        @NlsContexts.DialogTitle browseDialogTitle: String? = null,
        project: Project? = null,
        fileChooserDescriptor: FileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor(),
        fileChosen: ((chosenFile: VirtualFile) -> String)? = null
    ): ConceptCellBuilder<TextFieldWithBrowseButton> {
        val textField = textFieldWithBrowseButton(project, browseDialogTitle, fileChooserDescriptor, fileChosen)
        textField.text = modelBinding.get()
        return component(textField)
            .constraints(growX)
            .withBinding(TextFieldWithBrowseButton::getText, TextFieldWithBrowseButton::setText, modelBinding)
    }

    fun textFieldWithBrowseButton(
        property: GraphProperty<String>,
        emptyTextProperty: GraphProperty<String>,
        @NlsContexts.DialogTitle browseDialogTitle: String? = null,
        project: Project? = null,
        fileChooserDescriptor: FileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor(),
        fileChosen: ((chosenFile: VirtualFile) -> String)? = null
    ): ConceptCellBuilder<TextFieldWithBrowseButton> {
        return textFieldWithBrowseButton(property, browseDialogTitle, project, fileChooserDescriptor, fileChosen)
            .applyToComponent { emptyText.bind(emptyTextProperty) }
    }

    fun textFieldWithBrowseButton(
        property: GraphProperty<String>,
        @NlsContexts.DialogTitle browseDialogTitle: String? = null,
        project: Project? = null,
        fileChooserDescriptor: FileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor(),
        fileChosen: ((chosenFile: VirtualFile) -> String)? = null
    ): ConceptCellBuilder<TextFieldWithBrowseButton> {
        return textFieldWithBrowseButton(
            property::get,
            property::set,
            browseDialogTitle,
            project,
            fileChooserDescriptor,
            fileChosen
        )
            .withGraphProperty(property)
            .applyToComponent { bind(property) }
    }

    fun actionButton(
        action: AnAction,
        dimension: Dimension = ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE
    ): ConceptCellBuilder<ActionButton> {
        val actionButton = ActionButton(action, action.templatePresentation, ActionPlaces.UNKNOWN, dimension)
        return actionButton()
    }

    fun gearButton(vararg actions: AnAction): ConceptCellBuilder<JComponent> {
        val label = JLabel(LayeredIcon.GEAR_WITH_DROPDOWN)
        label.disabledIcon = AllIcons.General.GearPlain
        object : ClickListener() {
            override fun onClick(e: MouseEvent, clickCount: Int): Boolean {
                if (!label.isEnabled) return true
                JBPopupFactory.getInstance()
                    .createActionGroupPopup(null, DefaultActionGroup(*actions), DataContext { dataId ->
                        when (dataId) {
                            PlatformDataKeys.CONTEXT_COMPONENT.name -> label
                            else -> null
                        }
                    }, true, null, 10)
                    .showUnderneathOf(label)
                return true
            }
        }.installOn(label)

        return component(label)
    }

    fun expandableTextField(
        getter: () -> String,
        setter: (String) -> Unit,
        parser: Function<in String, out MutableList<String>> = ParametersListUtil.DEFAULT_LINE_PARSER,
        joiner: Function<in MutableList<String>, String> = ParametersListUtil.DEFAULT_LINE_JOINER
    )
            : ConceptCellBuilder<ExpandableTextField> {
        return ExpandableTextField(parser, joiner)()
            .withBinding(
                { editor -> editor.text.orEmpty() },
                { editor, value -> editor.text = value },
                ConceptPropertyBinding(getter, setter)
            )
    }

    fun expandableTextField(
        prop: KMutableProperty0<String>,
        parser: Function<in String, out MutableList<String>> = ParametersListUtil.DEFAULT_LINE_PARSER,
        joiner: Function<in MutableList<String>, String> = ParametersListUtil.DEFAULT_LINE_JOINER
    )
            : ConceptCellBuilder<ExpandableTextField> {
        return expandableTextField(prop::get, prop::set, parser, joiner)
    }

    fun expandableTextField(
        prop: GraphProperty<String>,
        parser: Function<in String, out MutableList<String>> = ParametersListUtil.DEFAULT_LINE_PARSER,
        joiner: Function<in MutableList<String>, String> = ParametersListUtil.DEFAULT_LINE_JOINER
    )
            : ConceptCellBuilder<ExpandableTextField> {
        return expandableTextField(prop::get, prop::set, parser, joiner)
            .withGraphProperty(prop)
            .applyToComponent { bind(prop) }
    }

    /**
     * @see LayoutBuilder.titledRow
     */
    @JvmOverloads
    fun panel(
        @NlsContexts.BorderTitle title: String,
        wrappedComponent: Component,
        hasSeparator: Boolean = true
    ): ConceptCellBuilder<JPanel> {
        val panel = Panel(title, hasSeparator)
        panel.add(wrappedComponent)
        return component(panel)
    }

    fun scrollPane(component: Component): ConceptCellBuilder<JScrollPane> {
        return component(JBScrollPane(component))
    }

    fun comment(@NlsContexts.DetailedDescription text: String, maxLineLength: Int = -1): ConceptCellBuilder<JLabel> {
        return component(ComponentPanelBuilder.createCommentComponent(text, true, maxLineLength, true))
    }

    fun commentNoWrap(@NlsContexts.DetailedDescription text: String): ConceptCellBuilder<JLabel> {
        return component(ComponentPanelBuilder.createNonWrappingCommentComponent(text))
    }

    fun placeholder(): ConceptCellBuilder<JComponent> {
        return component(JPanel().apply {
            minimumSize = Dimension(0, 0)
            preferredSize = Dimension(0, 0)
            maximumSize = Dimension(0, 0)
        })
    }

    abstract fun <T : JComponent> component(component: T): ConceptCellBuilder<T>

    abstract fun <T : JComponent> component(component: T, viewComponent: JComponent): ConceptCellBuilder<T>

    operator fun <T : JComponent> T.invoke(
        vararg constraints: CCFlags,
        growPolicy: ConceptGrowPolicy? = null,
        @NlsContexts.DetailedDescription comment: String? = null
    ): ConceptCellBuilder<T> = component(this).apply {
        constraints(*constraints)
        if (comment != null) comment(comment)
        if (growPolicy != null) growPolicy(growPolicy)
    }
}

private fun JBCheckBox.bind(property: GraphProperty<Boolean>) {
    val mutex = AtomicBoolean()
    property.afterChange {
        mutex.lockOrSkip {
            isSelected = property.get()
        }
    }
    addItemListener {
        mutex.lockOrSkip {
            property.set(isSelected)
        }
    }
}

class ConceptInnerCell(val cell: ConceptCell) : ConceptCell() {
    override fun <T : JComponent> component(component: T): ConceptCellBuilder<T> {
        return cell.component(component)
    }

    override fun <T : JComponent> component(component: T, viewComponent: JComponent): ConceptCellBuilder<T> {
        return cell.component(component, viewComponent)
    }

    override fun withButtonGroup(title: String?, buttonGroup: ButtonGroup, body: () -> Unit) {
        cell.withButtonGroup(title, buttonGroup, body)
    }
}

fun <T> listCellRenderer(renderer: SimpleListCellRenderer<T?>.(value: T, index: Int, isSelected: Boolean) -> Unit): SimpleListCellRenderer<T?> {
    return object : SimpleListCellRenderer<T?>() {
        override fun customize(list: JList<out T?>, value: T?, index: Int, selected: Boolean, hasFocus: Boolean) {
            if (value != null) {
                renderer(this, value, index, selected)
            }
        }
    }
}

fun <T> ComboBox<T>.bind(property: GraphProperty<T>) {
    val mutex = AtomicBoolean()
    property.afterChange {
        mutex.lockOrSkip {
            selectedItem = it
        }
    }
    addItemListener {
        if (it.stateChange == ItemEvent.SELECTED) {
            mutex.lockOrSkip {
                @Suppress("UNCHECKED_CAST")
                property.set(it.item as T)
            }
        }
    }
}

fun <T> ReferenceEditorComboWithBrowseButton.bind(property: GraphProperty<T>) {
    (childComponent as ComboBox<T>).bind(property)
}

private val TextFieldWithBrowseButton.emptyText
    get() = (textField as JBTextField).emptyText

fun StatusText.bind(property: GraphProperty<String>) {
    text = property.get()
    property.afterChange {
        text = it
    }
    property.afterReset {
        text = property.get()
    }
}

fun TextFieldWithBrowseButton.bind(property: GraphProperty<String>) {
    textField.bind(property)
}

fun JTextComponent.bind(property: GraphProperty<String>) {
    val mutex = AtomicBoolean()
    property.afterChange {
        mutex.lockOrSkip {
            text = it
        }
    }
    document.addDocumentListener(
        object : DocumentAdapter() {
            override fun textChanged(e: DocumentEvent) {
                mutex.lockOrSkip {
                    property.set(text)
                }
            }
        }
    )
}

private fun AtomicBoolean.lockOrSkip(action: () -> Unit) {
    if (!compareAndSet(false, true)) return
    try {
        action()
    } finally {
        set(false)
    }
}

fun ConceptCell.slider(min: Int, max: Int, minorTick: Int, majorTick: Int): ConceptCellBuilder<JSlider> {
    val slider = JSlider()
    UIUtil.setSliderIsFilled(slider, true)
    slider.paintLabels = true
    slider.paintTicks = true
    slider.paintTrack = true
    slider.minimum = min
    slider.maximum = max
    slider.minorTickSpacing = minorTick
    slider.majorTickSpacing = majorTick
    return slider()
}

fun <T : JSlider> ConceptCellBuilder<T>.labelTable(table: Hashtable<Int, JComponent>.() -> Unit): ConceptCellBuilder<T> {
    component.labelTable = Hashtable<Int, JComponent>().apply(table)
    return this
}

fun <T : JSlider> ConceptCellBuilder<T>.withValueBinding(modelBinding: ConceptPropertyBinding<Int>): ConceptCellBuilder<T> {
    return withBinding(JSlider::getValue, JSlider::setValue, modelBinding)
}