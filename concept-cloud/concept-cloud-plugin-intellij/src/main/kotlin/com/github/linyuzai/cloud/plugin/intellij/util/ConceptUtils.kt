package com.github.linyuzai.cloud.plugin.intellij.util

import com.github.linyuzai.cloud.plugin.intellij.ConceptCellBuilder
import com.github.linyuzai.cloud.plugin.intellij.ConceptTextValidationFunction
import com.github.linyuzai.cloud.plugin.intellij.withValidationForEditorCombo
import com.github.linyuzai.cloud.plugin.intellij.withValidationForText
import javax.swing.JComponent

fun <T : JComponent> ConceptCellBuilder<T>.withTextValidation(
    dialog: ConceptDialog,
    vararg errorValidations: ConceptTextValidationFunction,
): ConceptCellBuilder<T> {
    return withValidationForText(this, errorValidations.toList(), null, dialog)
}

fun <T : JComponent> ConceptCellBuilder<T>.withPackageValidation(
    dialog: ConceptDialog,
    vararg errorValidations: ConceptTextValidationFunction,
): ConceptCellBuilder<T> {
    return withValidationForEditorCombo(this, errorValidations.toList(), null, dialog)
}

fun <T : JComponent> ConceptCellBuilder<T>.withClassValidation(
    dialog: ConceptDialog,
    vararg errorValidations: ConceptTextValidationFunction,
): ConceptCellBuilder<T> {
    return withValidationForEditorCombo(this, errorValidations.toList(), null, dialog)
}