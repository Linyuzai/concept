package com.github.linyuzai.cloud.plugin.intellij.custom

import com.github.linyuzai.cloud.plugin.intellij.GenerateDomainAndModuleAction.Settings
import com.github.linyuzai.cloud.plugin.intellij.panel
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.RecentsManager
import com.intellij.ui.layout.LCFlags

object CustomComponents {

    @JvmStatic
    fun createGenerateDomainAndModule(project: Project, settings: Settings): DialogPanel {

        return panel(LCFlags.fillX, LCFlags.fillY) {

            row("Name:") {
                textField(settings.nameProperty)
            }

            row("User Domain Class:") {
                classesComboBox(
                    project, "GenerateDomainAndModule@User",
                    settings.userClassNameProperty
                )
                /*RecentsManager.getInstance(project)
                    .registerRecentEntry("GenerateDomainAndModule@User", settings.userClassNameProperty.get())*/
            }

            row("Domains Module:") {
                modulesComboBox(project, settings.domainModuleProperty)
            }

            row("Modules Module:") {
                modulesComboBox(project, settings.moduleModuleProperty)
            }
        }
    }
}