package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.util.NlsContexts
import com.intellij.ui.components.DialogPanel
import com.intellij.ui.layout.LCFlags

inline fun panel(vararg constraints: LCFlags, @NlsContexts.DialogTitle title: String? = null, init: ConceptLayoutBuilder.() -> Unit): DialogPanel {
    val builder = createLayoutBuilder()
    builder.init()

    val panel = DialogPanel(title, layout = null)
    builder.builder.build(panel, constraints)
    initPanel(builder, panel)
    return panel
}

@PublishedApi
internal fun initPanel(builder: ConceptLayoutBuilder, panel: DialogPanel) {
    panel.preferredFocusedComponent = builder.builder.preferredFocusedComponent
    panel.validateCallbacks = builder.builder.validateCallbacks
    panel.componentValidateCallbacks = builder.builder.componentValidateCallbacks
    panel.customValidationRequestors = builder.builder.customValidationRequestors
    panel.applyCallbacks = builder.builder.applyCallbacks
    panel.resetCallbacks = builder.builder.resetCallbacks
    panel.isModifiedCallbacks = builder.builder.isModifiedCallbacks
}