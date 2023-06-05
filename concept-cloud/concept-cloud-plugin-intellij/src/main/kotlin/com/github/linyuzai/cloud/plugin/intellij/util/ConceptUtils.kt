package com.github.linyuzai.cloud.plugin.intellij.util

import com.github.linyuzai.cloud.plugin.intellij.ConceptCellBuilder
import com.github.linyuzai.cloud.plugin.intellij.withValidationForEditorCombo
import com.github.linyuzai.cloud.plugin.intellij.withValidationForText
import com.intellij.ide.starters.shared.TextValidationFunction
import javax.swing.JComponent

fun <T : JComponent> ConceptCellBuilder<T>.withTextValidation(
    dialog: ConceptDialog,
    vararg errorValidations: TextValidationFunction,
): ConceptCellBuilder<T> {
    return withValidationForText(this, errorValidations.toList(), null, dialog)
}

fun <T : JComponent> ConceptCellBuilder<T>.withPackageValidation(
    dialog: ConceptDialog,
    vararg errorValidations: TextValidationFunction,
): ConceptCellBuilder<T> {
    return withValidationForEditorCombo(this, errorValidations.toList(), null, dialog)
}

fun <T : JComponent> ConceptCellBuilder<T>.withClassValidation(
    dialog: ConceptDialog,
    vararg errorValidations: TextValidationFunction,
): ConceptCellBuilder<T> {
    return withValidationForEditorCombo(this, errorValidations.toList(), null, dialog)
}