package com.github.linyuzai.cloud.plugin.intellij.module

import com.github.linyuzai.cloud.plugin.intellij.CHECK_NOT_EMPTY
import com.github.linyuzai.cloud.plugin.intellij.GenerateCodeAction
import com.github.linyuzai.cloud.plugin.intellij.panel
import com.github.linyuzai.cloud.plugin.intellij.util.ConceptDialog
import com.github.linyuzai.cloud.plugin.intellij.util.withClassValidation
import com.github.linyuzai.cloud.plugin.intellij.util.withPackageValidation
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.ui.DocumentAdapter
import com.intellij.ui.layout.LCFlags

object ModuleComponents {

    @JvmStatic
    fun createGenerateModuleCodeDialog(project: Project, model: ModuleModel, title: String): ConceptDialog {
        val dialog = ConceptDialog(project)
        dialog.setTitle(title)
        val panel = createGenerateModulePanel(project, model, dialog)
        dialog.setCenterPanel(panel)
        return dialog
    }

    @JvmStatic
    fun createGenerateModulePanel(project: Project, model: ModuleModel, dialog: ConceptDialog): DialogPanel {

        return panel(LCFlags.fillX, LCFlags.fillY) {

            row("User Domain Class:") {
                classesComboBox(
                    project,
                    GenerateCodeAction.RECENTS_KEY_USER_DOMAIN_CLASS,
                    model.userClass
                ).withClassValidation(
                    dialog,
                    CHECK_NOT_EMPTY
                )
            }

            row("Login Annotation Class:") {
                classesComboBox(
                    project,
                    GenerateCodeAction.RECENTS_KEY_LOGIN_ANNOTATION_CLASS,
                    model.loginAnnotationClass
                )
            }

            row("Module Module (.main):") {
                modulesComboBox(project, model.moduleModule)
            }

            row("Module Package:") {
                packagesComboBox(
                    project,
                    ModuleModel.RECENTS_KEY_MODULE_PACKAGE,
                    model.modulePackage
                ).withPackageValidation(
                    dialog,
                    CHECK_NOT_EMPTY
                )
            }

            row("Domain Object Class:") {
                classesComboBox(
                    project,
                    ModuleModel.RECENTS_KEY_MODULE_DOMAIN_OBJECT_CLASS,
                    model.domainObjectClass
                ) {
                    childComponent.addDocumentListener(object : DocumentListener {

                        override fun documentChanged(event: DocumentEvent) {
                            //childComponent.removeDocumentListener(this)
                            if (model.isAutoFindEnabled()) {
                                val psiClass = JavaPsiFacade.getInstance(project)
                                    .findClass(text, GlobalSearchScope.allScope(project))
                                if (psiClass == null) {
                                    return
                                }
                                val classes: Array<PsiClass> = ModuleModel
                                    .getClassesInPackage(project, psiClass)
                                if (model.autoFindDomainCollectionClass) {
                                    val collectionClass = ModuleModel.getDomainCollectionClass(classes)
                                    if (collectionClass != null) {
                                        val qualifiedName = ModuleModel.getQualifiedName(collectionClass)
                                        model.domainCollectionClass.set(qualifiedName)
                                        model.autoFindDomainCollectionClass = true
                                    }
                                }

                                if (model.autoFindDomainRepositoryClass) {
                                    val repositoryClass = ModuleModel.getDomainRepositoryClass(classes)
                                    if (repositoryClass != null) {
                                        val qualifiedName = ModuleModel.getQualifiedName(repositoryClass)
                                        model.domainRepositoryClass.set(qualifiedName)
                                        model.autoFindDomainRepositoryClass = true
                                    }
                                }

                                if (model.autoFindDomainServiceClass) {
                                    val serviceClass = ModuleModel.getClassByName(
                                        classes,
                                        "${psiClass.name}Service"
                                    )
                                    if (serviceClass != null) {
                                        val qualifiedName = ModuleModel.getQualifiedName(serviceClass)
                                        model.domainServiceClass.set(qualifiedName)
                                        model.autoFindDomainServiceClass = true
                                    }
                                }

                                if (model.autoFindDomainDescription) {
                                    val comment = ModuleModel.getDomainDescriptionByComment(psiClass)
                                    model.domainDescription.set(comment)
                                    model.autoFindDomainDescription = true
                                }
                            }
                        }
                    })
                }.withClassValidation(
                    dialog,
                    CHECK_NOT_EMPTY
                )
            }

            row("Domain Collection Class:") {
                classesComboBox(
                    project,
                    ModuleModel.RECENTS_KEY_MODULE_DOMAIN_COLLECTION_CLASS,
                    model.domainCollectionClass
                ) {
                    childComponent.addDocumentListener(object : DocumentListener {

                        override fun documentChanged(event: DocumentEvent) {
                            model.autoFindDomainCollectionClass = false
                        }
                    })
                }.withClassValidation(
                    dialog,
                    CHECK_NOT_EMPTY
                )
            }

            row("Domain Repository Class:") {
                classesComboBox(
                    project,
                    ModuleModel.RECENTS_KEY_MODULE_DOMAIN_REPOSITORY_CLASS,
                    model.domainRepositoryClass
                ) {
                    childComponent.addDocumentListener(object : DocumentListener {

                        override fun documentChanged(event: DocumentEvent) {
                            model.autoFindDomainRepositoryClass = false
                        }
                    })
                }.withClassValidation(
                    dialog,
                    CHECK_NOT_EMPTY
                )
            }

            row("Domain Service Class:") {
                classesComboBox(
                    project,
                    ModuleModel.RECENTS_KEY_MODULE_DOMAIN_SERVICE_CLASS,
                    model.domainServiceClass
                ) {
                    childComponent.addDocumentListener(object : DocumentListener {

                        override fun documentChanged(event: DocumentEvent) {
                            model.autoFindDomainServiceClass = false
                        }
                    })
                }.withClassValidation(
                    dialog,
                    CHECK_NOT_EMPTY
                )
            }

            row("Domain Description:") {
                textField(model.domainDescription) {
                    document.addDocumentListener(object : DocumentAdapter() {

                        override fun textChanged(e: javax.swing.event.DocumentEvent) {
                            model.autoFindDomainDescription = false
                        }
                    })
                }
            }

            row("Infrastructure:") {
                checkBox("MyBatisPlus", model.myBatisPlus)
            }
        }
    }
}