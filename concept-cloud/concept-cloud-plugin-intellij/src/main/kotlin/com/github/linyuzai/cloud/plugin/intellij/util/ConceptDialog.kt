package com.github.linyuzai.cloud.plugin.intellij.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogBuilder
import javax.swing.JComponent

class ConceptDialog(project: Project) : DialogBuilder(project) {

    val validatedComponents: MutableList<JComponent> = mutableListOf()
}