package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.observable.properties.transform
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile

fun ConceptRow.projectLocationField(
    locationProperty: GraphProperty<String>,
    wizardContext: WizardContext
): ConceptCellBuilder<TextFieldWithBrowseButton> {
    val fileChooserDescriptor =
        FileChooserDescriptorFactory.createSingleLocalFileDescriptor().withFileFilter { it.isDirectory }
    val fileChosen = { file: VirtualFile -> getUiFilePath(file.path) }
    val title = "Select ${wizardContext.presentationName} File Directory"
    //val property = locationProperty.map { getUiFilePath(it) }.comap { getModelFilePath(it) }
    val property = locationProperty
        .transform({ getUiFilePath(it) }, { it })
        .transform({ it }, { getModelFilePath(it) })
    return this.textFieldWithBrowseButton(property, title, wizardContext.project, fileChooserDescriptor, fileChosen)
}

internal fun getUiFilePath(path: String): String {
    return FileUtil.getLocationRelativeToUserHome(FileUtil.toSystemDependentName(path.trim()), false)
}

internal fun getModelFilePath(path: String): String {
    return FileUtil.toCanonicalPath(FileUtil.expandUserHome(path.trim()))
}