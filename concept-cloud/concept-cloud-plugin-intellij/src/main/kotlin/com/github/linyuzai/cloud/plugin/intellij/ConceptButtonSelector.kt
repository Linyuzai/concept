package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.actionSystem.ex.ActionButtonLook
import com.intellij.openapi.actionSystem.impl.ActionButton
import com.intellij.openapi.actionSystem.impl.ActionButtonWithText
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.NlsActions
import java.awt.Dimension
import java.awt.Insets
import java.util.function.Supplier
import kotlin.math.max

fun <T> ConceptRow.buttonSelector(options: Collection<T>, property: ConceptGraphProperty<T>, renderer: (T) -> String): ConceptButtonSelectorToolbar {
    val actionGroup = DefaultActionGroup(options.map { ButtonSelectorAction(it, property, renderer(it)) })
    val toolbar = ConceptButtonSelectorToolbar("ButtonSelector", actionGroup, true)
    toolbar.targetComponent = null // any data context is supported, suppress warning
    component(toolbar)
    return toolbar
}

class ButtonSelectorAction<T> @JvmOverloads constructor(private val option: T,
                                                        private val property: ConceptGraphProperty<T>,
                                                        optionText: Supplier<@NlsActions.ActionText String>,
                                                        optionDescription: Supplier<@NlsActions.ActionText String>? = null)
    : ToggleAction(optionText, optionDescription ?: Supplier { null }, null), DumbAware {

    @JvmOverloads
    constructor(option: T,
                property: ConceptGraphProperty<T>,
                @NlsActions.ActionText optionText: String,
                @NlsActions.ActionDescription optionDescription: String? = null) :
            this(option, property, Supplier { optionText }, optionDescription?.let { Supplier { optionDescription } })

    override fun isSelected(e: AnActionEvent): Boolean {
        return property.get() == option
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        if (state) {
            property.set(option)
        }
    }
}

private const val LEFT_RIGHT_PADDING: Int = 8
private const val TOP_BOTTOM_PADDING: Int = 2
private const val BUTTONS_MARGIN: Int = 2

private class ConceptButtonSelector(
    action: ButtonSelectorAction<*>,
    presentation: Presentation,
    place: String,
    minimumSize: Dimension,
    private val forceFieldHeight: Boolean
) : ActionButtonWithText(action, presentation, place, minimumSize) {
    init {
        isFocusable = true
    }

    override fun getInsets(): Insets = super.getInsets().apply {
        right += left + BUTTONS_MARGIN
        left = 0
    }

    override fun getPreferredSize(): Dimension {
        val old = super.getPreferredSize()
        val proposedHeight = old.height + TOP_BOTTOM_PADDING * 2
        val height = if (forceFieldHeight) max(30, proposedHeight) else proposedHeight
        return Dimension(old.width + LEFT_RIGHT_PADDING * 2, height)
    }
}

class ConceptButtonSelectorToolbar @JvmOverloads constructor(
    place: String,
    actionGroup: ActionGroup,
    horizontal: Boolean,
    private val forceFieldHeight: Boolean = false
) : ActionToolbarImpl(place, actionGroup, horizontal) {

    init {
        setForceMinimumSize(true)
    }

    override fun getPreferredSize(): Dimension {
        val size = super.getPreferredSize()
        return Dimension(size.width, max(30, size.height)) // there can be non-default font-size
    }

    override fun getMinimumSize(): Dimension {
        val size = super.getMinimumSize()
        return Dimension(size.width, max(30, size.height)) // there can be non-default font-size
    }

    init {
        layoutPolicy = ActionToolbar.WRAP_LAYOUT_POLICY
        isFocusable = false
    }

    override fun createToolbarButton(
        action: AnAction,
        look: ActionButtonLook?,
        place: String,
        presentation: Presentation,
        minimumSize: Dimension
    ): ActionButton = ConceptButtonSelector(action as ButtonSelectorAction<*>, presentation, place, minimumSize, forceFieldHeight)
}