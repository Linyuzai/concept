package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.layout.LCFlags
import java.awt.Container
import javax.swing.ButtonGroup
import javax.swing.JComponent

@PublishedApi
internal fun createLayoutBuilder(): ConceptLayoutBuilder {
    return ConceptLayoutBuilder(ConceptMigLayoutBuilder(createIntelliJSpacingConfiguration()))
}


interface ConceptLayoutBuilderImpl {
    val rootRow: ConceptRow
    fun withButtonGroup(buttonGroup: ButtonGroup, body: () -> Unit)

    fun build(container: Container, layoutConstraints: Array<out ConceptLCFlags>)

    val preferredFocusedComponent: JComponent?

    // Validators applied when Apply is pressed
    val validateCallbacks: List<() -> ValidationInfo?>

    // Validators applied immediately on input
    val componentValidateCallbacks: Map<JComponent, () -> ValidationInfo?>

    // Validation applicants for custom validation events
    val customValidationRequestors: Map<JComponent, List<(() -> Unit) -> Unit>>

    val applyCallbacks: Map<JComponent?, List<() -> Unit>>
    val resetCallbacks: Map<JComponent?, List<() -> Unit>>
    val isModifiedCallbacks: Map<JComponent?, List<() -> Boolean>>
}
