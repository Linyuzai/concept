package com.github.linyuzai.cloud.plugin.intellij.domain

import com.github.linyuzai.cloud.plugin.intellij.GenerateDomainCodeAction.Model
import com.github.linyuzai.cloud.plugin.intellij.panel
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.popup.IconButton
import com.intellij.ui.InplaceButton
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.layout.LCFlags
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.Dimension
import java.awt.event.AdjustmentEvent
import java.awt.event.AdjustmentListener

object DomainComponents {

    @JvmStatic
    fun showGenerateDomainCodeDialog(project: Project, model: Model, callback: () -> Unit) {
        val dialog = DialogBuilder(project)
        dialog.setTitle("Generate Domain Code")
        val panel = BorderLayoutPanel().apply {
            val dimension = Dimension(920, 0)
            size = dimension
            minimumSize = dimension
        }

        panel.addToCenter(createGenerateDomainPanel(project, model))
        panel.addToRight(createPreviewDomainPanel())
        dialog.setCenterPanel(panel)
        if (dialog.showAndGet()) {
            callback.invoke()
        }
    }

    @JvmStatic
    fun createGenerateDomainPanel(project: Project, model: Model): DialogPanel {

        /*val aClass = JavaPsiFacade.getInstance(project)
                .findClass(targetClassName, GlobalSearchScope.projectScope(project))*/

        return panel(LCFlags.fillX, LCFlags.fillY) {

            row("User Domain Class:") {
                classesComboBox(
                    project, null, "GenerateDomainAndModule@User", "Choose User Domain Class",
                    model.userClassNameProperty
                ) {

                }
            }

            row("Domains Module:") {
                modulesComboBox(project, model.domainModuleProperty)
            }

            row("Domain Class Name:") {
                textField(model.nameProperty)
            }

            row("Domain Class Props:") {
                val addButton = InplaceButton(
                    IconButton(
                        "Add",
                        AllIcons.General.InlineAdd, AllIcons.General.InlineAddHover
                    )
                ) {
                    model.addDomainProp()
                }
                component(addButton)
            }

            row {
                scrollPane(DomainPropsPanel(project).apply {
                    propRemoveListener = {
                        model.removeDomainProp(it.index)
                    }
                    model.addOnDomainPropAddListener { prop ->
                        addProp(prop)
                    }
                    model.addOnDomainPropRemoveListener { prop ->
                        removeProp(prop)
                    }
                }, minimumSize = Dimension(0, 200)) {
                    model.addOnDomainPropAddListener {
                        verticalScrollBar.apply {
                            addAdjustmentListener(object : AdjustmentListener {

                                override fun adjustmentValueChanged(e: AdjustmentEvent) {
                                    if (e.adjustmentType == AdjustmentEvent.TRACK) {
                                        removeAdjustmentListener(this)
                                        value = maximum - getModel().extent
                                    }
                                }
                            })
                        }
                    }
                }
                /*component(JPanel(GridBagLayout()).apply {
                    add(BorderLayoutPanel().apply {
                        minimumSize = Dimension(0, 200)
                        preferredSize = Dimension(0, 0)
                        addToCenter(ScrollPaneFactory.createScrollPane(DomainPropsPanel(project).apply {
                            propRemoveListener = {
                                model.removeDomainProp(it)
                                model.updateDomainProps()

                            }
                            model.addOnDomainPropUpdateListener {
                                update(it)
                            }
                        }))
                    }, gridConstraint(0, 0))
                }).constraints(push, grow)*/
            }
        }
    }

    fun createPreviewDomainPanel(): DialogPanel {
        return panel(LCFlags.fillX, LCFlags.fillY) {
            row("Preview:") {

            }

            row {
                scrollPane(JBTextArea(1, 40).apply {
                    isEditable = false
                })
            }
        }
    }

    @JvmStatic
    fun createGenerateDomainAndModule(project: Project, model: Model): DialogPanel {

        return panel(LCFlags.fillX, LCFlags.fillY) {

            row("Name:") {
                textField(model.nameProperty)
            }

            row("User Domain Class:") {
                /*classesComboBox(
                    project,
                    "GenerateDomainAndModule@User",
                    "Choose User Domain Class",
                    model.userClassNameProperty
                )*/
            }

            row("Domains Module:") {
                modulesComboBox(project, model.domainModuleProperty)
            }

            row("Modules Module:") {
                modulesComboBox(project, model.moduleModuleProperty)
            }
        }
    }
}