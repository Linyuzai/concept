package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.layout.*
import org.jetbrains.annotations.Nls
import java.awt.event.ActionListener
import javax.swing.AbstractButton
import javax.swing.ButtonGroup

open class ConceptLayoutBuilder @PublishedApi internal constructor(@PublishedApi internal val builder: ConceptLayoutBuilderImpl) : ConceptRowBuilder by builder.rootRow {
    override fun withButtonGroup(title: String?, buttonGroup: ButtonGroup, body: () -> Unit) {
        builder.withButtonGroup(buttonGroup, body)
    }

    inline fun buttonGroup(crossinline elementActionListener: () -> Unit, crossinline init: ConceptLayoutBuilder.() -> Unit): ButtonGroup {
        val group = ButtonGroup()

        builder.withButtonGroup(group) {
            ConceptLayoutBuilder(builder).init()
        }

        val listener = ActionListener { elementActionListener() }
        for (button in group.elements) {
            button.addActionListener(listener)
        }
        return group
    }

    @Suppress("PropertyName")
    @PublishedApi
    internal val `$`: ConceptLayoutBuilderImpl
        get() = builder
}

class ConceptCellBuilderWithButtonGroupProperty<T : Any>
@PublishedApi internal constructor(private val prop: ConceptPropertyBinding<T>)  {

    fun ConceptCell.radioButton(@NlsContexts.RadioButton text: String, value: T, @Nls comment: String? = null): ConceptCellBuilder<JBRadioButton> {
        val component = JBRadioButton(text, prop.get() == value)
        return component(comment = comment).bindValue(value)
    }

    fun ConceptCellBuilder<JBRadioButton>.bindValue(value: T): ConceptCellBuilder<JBRadioButton> = bindValueToProperty(prop, value)
}


class ConceptRowBuilderWithButtonGroupProperty<T : Any>
@PublishedApi internal constructor(private val builder: ConceptRowBuilder, private val prop: ConceptPropertyBinding<T>) : ConceptRowBuilder by builder {

    fun ConceptRow.radioButton(@NlsContexts.RadioButton text: String, value: T, @Nls comment: String? = null): ConceptCellBuilder<JBRadioButton> {
        val component = JBRadioButton(text, prop.get() == value)
        attachSubRowsEnabled(component)
        return component(comment = comment).bindValue(value)
    }

    fun ConceptCellBuilder<JBRadioButton>.bindValue(value: T): ConceptCellBuilder<JBRadioButton> = bindValueToProperty(prop, value)
}

private fun <T> ConceptCellBuilder<JBRadioButton>.bindValueToProperty(prop: ConceptPropertyBinding<T>, value: T): ConceptCellBuilder<JBRadioButton> = apply {
    onApply { if (component.isSelected) prop.set(value) }
    onReset { component.isSelected = prop.get() == value }
    onIsModified { component.isSelected != (prop.get() == value) }
}

fun FileChooserDescriptor.chooseFile(event: AnActionEvent, fileChosen: (chosenFile: VirtualFile) -> Unit) {
    FileChooser.chooseFile(this, event.getData(PlatformDataKeys.PROJECT), event.getData(PlatformDataKeys.CONTEXT_COMPONENT), null, fileChosen)
}

fun ConceptRow.attachSubRowsEnabled(component: AbstractButton) {
    enableSubRowsIf(component.selected)
}
