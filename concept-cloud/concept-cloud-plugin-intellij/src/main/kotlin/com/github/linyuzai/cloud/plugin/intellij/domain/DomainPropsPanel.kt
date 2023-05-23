package com.github.linyuzai.cloud.plugin.intellij.domain

import com.github.linyuzai.cloud.plugin.intellij.panel
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
import com.intellij.ui.DocumentAdapter
import com.intellij.ui.InplaceButton
import com.intellij.ui.JBColor
import com.intellij.ui.ReferenceEditorComboWithBrowseButton
import com.intellij.ui.components.JBTextField
import com.intellij.ui.components.panels.VerticalLayout
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.jbTextField
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.Cursor
import java.awt.Dimension
import java.awt.event.ActionListener

class DomainPropsPanel(val project: Project) : ScrollablePanel(VerticalLayout(UIUtil.DEFAULT_VGAP)) {

    var propRemoveListener: ((DomainProp) -> Unit)? = null

    init {

        border = JBUI.Borders.empty(5, 5, 5, 15)
        background = UIUtil.getListBackground()
    }

    fun addProp(prop: DomainProp, revalidate: Boolean = true) {

        val nameText = JBTextField().apply {
            document.addDocumentListener(object : DocumentAdapter() {

                override fun textChanged(e: javax.swing.event.DocumentEvent) {
                    prop.smartFill = false
                }
            })

            prop.onClassNameUpdateListener = {
                text = it
            }
        }


        val removeButton = InplaceButton(
            IconButton(
                "Remove",
                Actions.Close, Actions.CloseHovered
            )
        ) {
            propRemoveListener?.invoke(prop)
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        }

        var post: (() -> Unit)? = null

        val propPanel = BorderLayoutPanel()
        propPanel.border = JBUI.Borders.customLine(JBColor.border(), 0, 0, 1, 0)
        propPanel.background = UIUtil.getListBackground()

        propPanel.addToRight(removeButton)
        propPanel.addToCenter(panel(LCFlags.fillX, LCFlags.fillY) {

            row("Prop Class:") {
                classesComboBox(project,
                    prop,
                    "DomainProp",
                    "Choose Domain Prop Class",
                    { "" },
                    {}) {
                    removeButton.setTransform(0, -JBUIScale.scale(2.coerceAtLeast(font.size / 15)))
                    post = {
                        childComponent.requestFocus()
                        childComponent.requestFocusInWindow()
                    }
                }
            }.largeGapAfter()

            row("Prop Name:") {
                component(nameText)
            }.largeGapAfter()

            row("Prop Valid:") {
                checkBox("NotNull", { false }, {})
                checkBox("NotEmpty", { false }, {})
            }.largeGapAfter()

            row("Prop Comment:") {
                textField({ "" }, { })
            }.largeGapAfter()
        })

        add(propPanel)

        if (revalidate) {
            revalidate()
            post?.invoke()
        }
    }

    fun removeProp(prop: DomainProp, revalidate: Boolean = true) {
        remove(prop.index)
        if (revalidate) {
            revalidate()
        }
    }

    fun update(props: List<DomainProp>) {
        removeAll()

        for (prop in props) {

            addProp(prop, false)

        }

        revalidate()

    }

    /*private fun classesComboBox(project: Project): ReferenceEditorComboWithBrowseButton {
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
    }*/
}