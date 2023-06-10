package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.DialogPanel
import com.intellij.ui.layout.LCFlags
import com.intellij.util.ui.JBUI
import javax.swing.border.Border

inline fun panel(
    vararg constraints: LCFlags,
    title: String? = null,
    border: Border = JBUI.Borders.empty(10, 20, 5, 20),
    init: ConceptLayoutBuilder.() -> Unit
): DialogPanel {
    val builder = createLayoutBuilder()
    builder.init()

    val panel = DialogPanel(title, layout = null).withBorder(border)
    builder.builder.build(panel, constraints)
    initPanel(builder, panel)
    return panel
}

@PublishedApi
internal fun initPanel(builder: ConceptLayoutBuilder, panel: DialogPanel) {
    panel.preferredFocusedComponent = builder.builder.preferredFocusedComponent
    panel.applyCallbacks = builder.builder.applyCallbacks
    panel.resetCallbacks = builder.builder.resetCallbacks
    panel.isModifiedCallbacks = builder.builder.isModifiedCallbacks
}