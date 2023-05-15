package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.ide.starters.shared.LibraryInfo
import com.intellij.icons.AllIcons.Actions
import com.intellij.openapi.roots.ui.componentsList.components.ScrollablePanel
import com.intellij.openapi.ui.popup.IconButton
import com.intellij.ui.InplaceButton
import com.intellij.ui.JBColor
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.panels.VerticalLayout
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.BorderLayout
import java.awt.Cursor
import javax.swing.JLabel

class ConceptSelectedLibrariesPanel : JBPanelWithEmptyText(BorderLayout()) {
    private val scrollablePanel: ScrollablePanel = ScrollablePanel(VerticalLayout(UIUtil.DEFAULT_VGAP))
    private val scrollPane = ScrollPaneFactory.createScrollPane(scrollablePanel, true)

    var libraryRemoveListener: ((ConceptLibraryInfo) -> Unit)? = null
    var dependencyStateFunction: ((ConceptLibraryInfo) -> ConceptDependencyState)? = null

    init {
        this.background = UIUtil.getListBackground()
        this.border = JBUI.Borders.customLine(JBColor.border(), 1)

        add(scrollPane, BorderLayout.CENTER)

        scrollablePanel.border = JBUI.Borders.empty(5)
        scrollablePanel.background = UIUtil.getListBackground()
        scrollPane.isVisible = false
    }

    fun update(libraries: Collection<ConceptLibraryInfo>) {
        scrollablePanel.removeAll()

        for (library in libraries) {
            if (library.isRequired) continue // required are not shown

            val dependencyPanel = BorderLayoutPanel()
            dependencyPanel.background = UIUtil.getListBackground()

            val dependencyLabel = JLabel(library.title)
            dependencyLabel.border = JBUI.Borders.empty(0, UIUtil.DEFAULT_HGAP / 2, UIUtil.DEFAULT_VGAP, 0)

            val dependencyStateFunction = this.dependencyStateFunction
            if (dependencyStateFunction != null) {
                val state = dependencyStateFunction.invoke(library)
                if (state is ConceptDependencyUnavailable) {
                    dependencyLabel.isEnabled = false
                    dependencyLabel.toolTipText = state.message
                }
            }

            val removeButton = InplaceButton(
                IconButton(
                    "Remove",
                    Actions.Close, Actions.CloseHovered
                )
            ) {
                libraryRemoveListener?.invoke(library)
            }
            removeButton.setTransform(0, -JBUIScale.scale(2.coerceAtLeast(dependencyLabel.font.size / 15)))
            removeButton.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)

            dependencyPanel.addToLeft(removeButton)
            dependencyPanel.addToCenter(dependencyLabel)

            scrollablePanel.add(dependencyPanel)
        }
        scrollPane.isVisible = scrollablePanel.componentCount > 0

        scrollablePanel.revalidate()
        scrollPane.revalidate()
        revalidate()
    }
}