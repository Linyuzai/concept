package com.github.linyuzai.cloud.plugin.intellij.domain

import com.intellij.icons.AllIcons.Actions
import com.intellij.ide.util.ClassFilter
import com.intellij.ide.util.TreeClassChooserFactory
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ui.componentsList.components.ScrollablePanel
import com.intellij.openapi.ui.popup.IconButton
import com.intellij.psi.JavaCodeFragment
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.ui.InplaceButton
import com.intellij.ui.JBColor
import com.intellij.ui.ReferenceEditorComboWithBrowseButton
import com.intellij.ui.components.JBTextField
import com.intellij.ui.components.panels.VerticalLayout
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.Cursor
import java.awt.event.ActionListener

class DomainPropsPanel(val project: Project) : ScrollablePanel(VerticalLayout(UIUtil.DEFAULT_VGAP)) {

    var propRemoveListener: ((DomainProp) -> Unit)? = null

    init {
        this.background = UIUtil.getListBackground()
        this.border = JBUI.Borders.customLine(JBColor.border(), 1)

        border = JBUI.Borders.empty(5)
        background = UIUtil.getListBackground()
    }

    fun update(props: Collection<DomainProp>) {
        removeAll()

        for (prop in props) {

            val propPanel = BorderLayoutPanel()
            propPanel.background = UIUtil.getListBackground()

            val classesComboBox = classesComboBox(project)
            classesComboBox.border = JBUI.Borders.empty(0, UIUtil.DEFAULT_HGAP / 2, UIUtil.DEFAULT_VGAP, 0)

            val nameText = JBTextField()

            classesComboBox.childComponent.apply {
                addDocumentListener(object : DocumentListener {

                    override fun documentChanged(event: DocumentEvent) {
                        nameText.text = text
                    }
                })
            }

            val removeButton = InplaceButton(
                IconButton(
                    "Remove",
                    Actions.Close, Actions.CloseHovered
                )
            ) {
                propRemoveListener?.invoke(prop)
            }
            removeButton.setTransform(0, -JBUIScale.scale(2.coerceAtLeast(classesComboBox.font.size / 15)))
            removeButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)

            propPanel.addToLeft(removeButton)
            propPanel.addToCenter(classesComboBox)
            propPanel.addToRight(nameText)

            add(propPanel)
        }

        revalidate()
    }

    private fun classesComboBox(project: Project): ReferenceEditorComboWithBrowseButton {
        return ReferenceEditorComboWithBrowseButton(
            null,
            "",
            project,
            true,
            JavaCodeFragment.VisibilityChecker.PROJECT_SCOPE_VISIBLE,
            "DomainProp"
        ).apply {
            addActionListener(ActionListener {
                val chooser = TreeClassChooserFactory.getInstance(project).createWithInnerClassesScopeChooser(
                    "Choose Domain Prop Class", GlobalSearchScope.projectScope(project),
                    ClassFilter.ALL, null
                )
                val targetClassName: String? = text
                if (targetClassName != null) {
                    val aClass = JavaPsiFacade.getInstance(project)
                        .findClass(targetClassName, GlobalSearchScope.projectScope(project))
                    if (aClass != null) {
                        chooser.selectDirectory(aClass.containingFile.containingDirectory)
                    }
                }
                chooser.showDialog()
                val aClass = chooser.selected
                if (aClass != null) {
                    text = aClass.qualifiedName
                }
            })
        }
    }
}