package com.github.linyuzai.cloud.plugin.intellij.custom

import com.github.linyuzai.cloud.plugin.intellij.GenerateDomainCodeAction.Settings
import com.github.linyuzai.cloud.plugin.intellij.domain.DomainProp
import com.github.linyuzai.cloud.plugin.intellij.domain.DomainPropsPanel
import com.github.linyuzai.cloud.plugin.intellij.gridConstraint
import com.github.linyuzai.cloud.plugin.intellij.panel
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.popup.IconButton
import com.intellij.ui.InplaceButton
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.layout.LCFlags
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.Dimension
import java.awt.GridBagLayout
import javax.swing.JPanel

object CustomComponents {

    @JvmStatic
    fun showGenerateDomainCodeDialog(project: Project, settings: Settings, callback: () -> Unit) {
        val dialog = DialogBuilder(project)
        dialog.setTitle("Generate Domain Code")
        dialog.setCenterPanel(createGenerateDomainPanel(project, settings))
        if (dialog.showAndGet()) {
            callback.invoke()
        }
    }

    @JvmStatic
    fun createGenerateDomainPanel(project: Project, settings: Settings): DialogPanel {

        /*val aClass = JavaPsiFacade.getInstance(project)
                .findClass(targetClassName, GlobalSearchScope.projectScope(project))*/

        return panel(LCFlags.fillX, LCFlags.fillY) {

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

            row("Domain Class Name:") {
                textField(settings.nameProperty)
            }

            var domainPropsPanel: DomainPropsPanel? = null
            val domainProps = mutableListOf<DomainProp>()

            row("Domain Class Props:") {
                val addButton = InplaceButton(
                    IconButton(
                        "Add",
                        AllIcons.General.InlineAdd, AllIcons.General.InlineAddHover
                    )
                ) {
                    domainProps.add(DomainProp())
                    domainPropsPanel?.update(domainProps)
                }
                component(addButton)
            }

            row {
                component(JPanel(GridBagLayout()).apply {
                    add(BorderLayoutPanel().apply {
                        preferredSize = Dimension(0, 0)
                        domainPropsPanel = DomainPropsPanel(project).apply {
                            propRemoveListener = {
                                domainProps.remove(it)
                            }
                            addToCenter(ScrollPaneFactory.createScrollPane(this))
                        }
                    }, gridConstraint(0, 0))
                }).constraints(push, grow)
            }
        }
    }

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