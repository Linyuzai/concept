package com.github.linyuzai.cloud.plugin.intellij.module

import com.github.linyuzai.cloud.plugin.intellij.GenerateCodeAction
import com.github.linyuzai.cloud.plugin.intellij.panel
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.layout.LCFlags

object ModuleComponents {

    @JvmStatic
    fun createGenerateModuleCodeDialog(project: Project, model: ModuleModel, title: String): DialogBuilder {
        val dialog = DialogBuilder(project)
        dialog.setTitle(title)
        val panel = createGenerateModulePanel(project, model)
        dialog.setCenterPanel(panel)
        return dialog
    }

    @JvmStatic
    fun createGenerateModulePanel(project: Project, model: ModuleModel): DialogPanel {

        return panel(LCFlags.fillX, LCFlags.fillY) {

            row("User Domain Class:") {
                classesComboBox(
                    project,
                    GenerateCodeAction.RECENTS_KEY_USER_DOMAIN_CLASS,
                    model.userClass
                ) {}
            }

            row("Login Annotation Class:") {
                classesComboBox(
                    project,
                    GenerateCodeAction.RECENTS_KEY_LOGIN_ANNOTATION_CLASS,
                    model.loginAnnotationClass
                ) {}
            }

            row("Module Module (.main):") {
                modulesComboBox(project, model.moduleModule)
            }

            row("Module Package:") {
                packagesComboBox(
                    project,
                    ModuleModel.RECENTS_KEY_MODULE_PACKAGE,
                    model.modulePackage
                )
            }

            row("Domain Object Class:") {
                classesComboBox(
                    project,
                    ModuleModel.RECENTS_KEY_MODULE_DOMAIN_OBJECT_CLASS,
                    model.domainObjectClass
                ) {}
            }

            row("Domain Collection Class:") {
                classesComboBox(
                    project,
                    ModuleModel.RECENTS_KEY_MODULE_DOMAIN_COLLECTION_CLASS,
                    model.domainCollectionClass
                ) {}
            }

            row("Domain Service Class:") {
                classesComboBox(
                    project,
                    ModuleModel.RECENTS_KEY_MODULE_DOMAIN_SERVICE_CLASS,
                    model.domainServiceClass
                ) {}
            }

            row("Domain Repository Class:") {
                classesComboBox(
                    project,
                    ModuleModel.RECENTS_KEY_MODULE_DOMAIN_REPOSITORY_CLASS,
                    model.domainRepositoryClass
                ) {
                    //childComponent.addDocumentListener()
                }
            }

            row("Domain Description:") {
                textField(model.domainDescription) {
                    //document.addDocumentListener()
                }
            }

            row("Infrastructure:") {
                checkBox("MyBatisPlus", model.myBatisPlus)
            }
        }
    }
}