package com.github.linyuzai.cloud.plugin.intellij.domain

import com.github.linyuzai.cloud.plugin.intellij.*
import com.github.linyuzai.cloud.plugin.intellij.util.ConceptDialog
import com.github.linyuzai.cloud.plugin.intellij.util.withClassValidation
import com.github.linyuzai.cloud.plugin.intellij.util.withPackageValidation
import com.github.linyuzai.cloud.plugin.intellij.util.withTextValidation
import com.intellij.ide.starters.shared.ValidationFunctions
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.*
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.layout.LCFlags
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.Dimension
import java.awt.event.AdjustmentEvent
import java.awt.event.AdjustmentListener

object DomainComponents {

    @JvmStatic
    fun createGenerateDomainCodeDialog(project: Project, model: DomainModel, title: String): ConceptDialog {
        val dialog = ConceptDialog(project)
        dialog.setTitle(title)
        val panel = BorderLayoutPanel().apply {
            val dimension = Dimension(1000, 500)
            //preferredSize = dimension
            minimumSize = dimension
        }

        val dialogPanel = createGenerateDomainPanel(project, model, dialog)

        panel.addToCenter(dialogPanel)
        panel.addToRight(createPreviewDomainPanel(model))
        dialog.setCenterPanel(panel)
        model.preview()
        return dialog
    }

    @JvmStatic
    fun createGenerateDomainPanel(
        project: Project,
        model: DomainModel,
        dialog: ConceptDialog
    ): DialogPanel {

        return panel(LCFlags.fillX, LCFlags.fillY) {

            row("User Domain Class:") {
                classesComboBox(
                    project,
                    GenerateCodeAction.RECENTS_KEY_USER_DOMAIN_CLASS,
                    model.userClass
                ).withClassValidation(
                    dialog,
                    ValidationFunctions.CHECK_NOT_EMPTY
                )
            }

            row("Domain Module (.main):") {
                modulesComboBox(project, model.domainModule)
            }

            row("Domain Package:") {
                packagesComboBox(
                    project,
                    DomainModel.RECENTS_KEY_DOMAIN_PACKAGE,
                    model.domainPackage
                ).withPackageValidation(
                    dialog,
                    ValidationFunctions.CHECK_NOT_EMPTY
                )
            }

            row("Domain Object Class Name:") {
                textField(model.domainObjectClassName)
                    .withTextValidation(
                        dialog,
                        ValidationFunctions.CHECK_NOT_EMPTY,
                        ValidationFunctions.CHECK_SIMPLE_NAME_FORMAT,
                    )
            }

            row("Domain Collection Class Name:") {
                textField(model.domainCollectionClassName)
                    .withTextValidation(
                        dialog,
                        ValidationFunctions.CHECK_NOT_EMPTY,
                        ValidationFunctions.CHECK_SIMPLE_NAME_FORMAT,
                    )
            }

            row("Domain Class Comment:") {
                textField(model.domainClassComment)
            }

            row("Domain Class Props:") {
                /*val addButton = InplaceButton(
                    IconButton(
                        "Add prop",
                        AllIcons.General.InlineAdd, AllIcons.General.InlineAddHover
                    )
                ) {
                    model.addDomainProp()
                }
                component(addButton)*/
                button("Add Prop") {
                    model.addDomainProp()
                }
            }

            row {
                scrollPane(DomainPropsPanel(project).apply {
                    propRemoveListener = {
                        model.removeDomainProp(it.index)
                    }
                    model.addOnDomainPropAddListener { prop ->
                        addProp(prop)
                        //model.preview()
                    }
                    model.addOnDomainPropRemoveListener { prop ->
                        removeProp(prop)
                        model.preview()
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

    fun createPreviewDomainPanel(model: DomainModel): DialogPanel {
        return panel(LCFlags.fillX, LCFlags.fillY) {
            row("Preview (pseudo code):") {

            }

            row {
                //scrollableTextArea({""},{})
                scrollPane(JBTextArea(1, 40).apply {
                    isEditable = false
                    model.domainPreview.afterChange {
                        text = it
                    }
                })
            }
        }
    }
}