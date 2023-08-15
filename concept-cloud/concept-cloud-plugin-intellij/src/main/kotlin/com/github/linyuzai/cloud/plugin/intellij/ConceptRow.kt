package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.panel.ComponentPanelBuilder
import com.intellij.ui.components.Label
import com.intellij.ui.components.noteComponent
import com.intellij.ui.layout.*
import org.jetbrains.annotations.Nls
import javax.swing.ButtonGroup
import javax.swing.JComponent
import javax.swing.JLabel
import kotlin.reflect.KMutableProperty0

interface ConceptBaseBuilder {
    fun withButtonGroup(title: String?, buttonGroup: ButtonGroup, body: () -> Unit)

    fun withButtonGroup(buttonGroup: ButtonGroup, body: () -> Unit) {
        withButtonGroup(null, buttonGroup, body)
    }

    fun buttonGroup(init: () -> Unit) {
        buttonGroup(null, init)
    }

    fun buttonGroup(title: String? = null, init: () -> Unit) {
        withButtonGroup(title, ButtonGroup(), init)
    }
}

interface ConceptRowBuilder : ConceptBaseBuilder {
    fun createChildRow(
        label: JLabel? = null,
        isSeparated: Boolean = false,
        noGrid: Boolean = false,
        @Nls title: String? = null
    ): ConceptRow

    fun createNoteOrCommentRow(component: JComponent): ConceptRow

    fun checkBoxGroup(@Nls title: String?, body: () -> Unit)

    fun row(label: JLabel? = null, separated: Boolean = false, init: ConceptRow.() -> Unit): ConceptRow {
        return createChildRow(label = label, isSeparated = separated).apply(init)
    }

    fun row(@Nls label: String?, separated: Boolean = false, init: ConceptRow.() -> Unit): ConceptRow {
        return createChildRow(label?.let { Label(it) }, isSeparated = separated).apply(init)
    }

    fun titledRow(title: String, init: ConceptRow.() -> Unit): ConceptRow

    /**
     * Creates row with a huge gap after it, that can be used to group related components.
     * Think of [titledRow] without a title and additional indent.
     */
    fun blockRow(init: ConceptRow.() -> Unit): ConceptRow

    /**
     * Creates row with hideable decorator.
     * It allows to hide some information under the titled decorator
     */
    fun hideableRow(title: String, init: ConceptRow.() -> Unit): ConceptRow

    /**
     * Hyperlinks are supported (`<a href=""></a>`), new lines and `<br>` are supported only if no links (file issue if need).
     */
    fun noteRow(@Nls text: String, linkHandler: ((url: String) -> Unit)? = null) {
        createNoteOrCommentRow(noteComponent(text, linkHandler))
    }

    fun commentRow(@Nls text: String) {
        createNoteOrCommentRow(ComponentPanelBuilder.createCommentComponent(text, true, -1, true))
    }

    /**
     * Creates a nested UI DSL panel, with a grid which is independent of this pane.
     */
    fun nestedPanel(title: String? = null, init: ConceptLayoutBuilder.() -> Unit): ConceptCellBuilder<DialogPanel>

    fun onGlobalApply(callback: () -> Unit): ConceptRow
    fun onGlobalReset(callback: () -> Unit): ConceptRow
    fun onGlobalIsModified(callback: () -> Boolean): ConceptRow
}

inline fun <reified T : Any> ConceptInnerCell.buttonGroup(
    prop: KMutableProperty0<T>,
    crossinline init: ConceptCellBuilderWithButtonGroupProperty<T>.() -> Unit
) {
    buttonGroup(prop.toBinding(), init)
}

inline fun <reified T : Any> ConceptInnerCell.buttonGroup(
    noinline getter: () -> T,
    noinline setter: (T) -> Unit,
    crossinline init: ConceptCellBuilderWithButtonGroupProperty<T>.() -> Unit
) {
    buttonGroup(ConceptPropertyBinding(getter, setter), init)
}

inline fun <reified T : Any> ConceptInnerCell.buttonGroup(
    binding: ConceptPropertyBinding<T>,
    crossinline init: ConceptCellBuilderWithButtonGroupProperty<T>.() -> Unit
) {
    withButtonGroup(ButtonGroup()) {
        ConceptCellBuilderWithButtonGroupProperty(binding).init()
    }
}

inline fun <reified T : Any> ConceptRowBuilder.buttonGroup(
    prop: KMutableProperty0<T>,
    crossinline init: ConceptRowBuilderWithButtonGroupProperty<T>.() -> Unit
) {
    buttonGroup(prop.toBinding(), init)
}

inline fun <reified T : Any> ConceptRowBuilder.buttonGroup(
    noinline getter: () -> T,
    noinline setter: (T) -> Unit,
    crossinline init: ConceptRowBuilderWithButtonGroupProperty<T>.() -> Unit
) {
    buttonGroup(ConceptPropertyBinding(getter, setter), init)
}

inline fun <reified T : Any> ConceptRowBuilder.buttonGroup(
    binding: ConceptPropertyBinding<T>,
    crossinline init: ConceptRowBuilderWithButtonGroupProperty<T>.() -> Unit
) {
    withButtonGroup(ButtonGroup()) {
        ConceptRowBuilderWithButtonGroupProperty(this, binding).init()
    }
}

abstract class ConceptRow : ConceptCell(), ConceptRowBuilder {
    abstract var enabled: Boolean

    abstract var visible: Boolean

    abstract var subRowsEnabled: Boolean

    abstract var subRowsVisible: Boolean

    /**
     * Indent for child rows of this row, expressed in steps (multiples of [SpacingConfiguration.indentLevel]). Replaces indent
     * calculated from row nesting.
     */
    abstract var subRowIndent: Int

    protected abstract val builder: ConceptLayoutBuilderImpl

    /**
     * Specifies the right alignment for the component if the cell is larger than the component plus its gaps.
     */
    inline fun right(init: ConceptRow.() -> Unit) {
        alignRight()
        init()
    }

    @PublishedApi
    internal abstract fun alignRight()

    abstract fun largeGapAfter()

    /**
     * Shares cell between components.
     *
     * @param isFullWidth If `true`, the cell occupies the full width of the enclosing component.
     */
    inline fun cell(isVerticalFlow: Boolean = false, isFullWidth: Boolean = false, init: ConceptInnerCell.() -> Unit) {
        setCellMode(true, isVerticalFlow, isFullWidth)
        ConceptInnerCell(this).init()
        setCellMode(false, isVerticalFlow, isFullWidth)
    }

    @PublishedApi
    internal abstract fun createRow(label: String?): ConceptRow

    @PublishedApi
    internal abstract fun setCellMode(value: Boolean, isVerticalFlow: Boolean, fullWidth: Boolean)

    // backward compatibility
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "deprecated")
    operator fun JComponent.invoke(
        vararg constraints: ConceptCCFlags,
        gapLeft: Int = 0,
        growPolicy: ConceptGrowPolicy? = null
    ) {
        invoke(constraints = *constraints, growPolicy = growPolicy).withLeftGap(gapLeft)
    }
}

enum class ConceptGrowPolicy {
    SHORT_TEXT, MEDIUM_TEXT
}

fun ConceptRow.enableIf(predicate: ComponentPredicate) {
    enabled = predicate()
    predicate.addListener { enabled = it }
}

fun ConceptRow.enableSubRowsIf(predicate: ComponentPredicate) {
    subRowsEnabled = predicate()
    predicate.addListener { subRowsEnabled = it }
}
