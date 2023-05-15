package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.NlsContexts
import com.intellij.ui.TitledSeparator
import java.awt.Cursor
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class ConceptHideableTitledSeparator(@NlsContexts.Separator title: String) : TitledSeparator(title) {

    private var isExpanded: Boolean = true

    lateinit var row: ConceptRow

    fun expand() = update(true)

    fun collapse() = update(false)

    private fun update(expand: Boolean) {
        isExpanded = expand
        row.visible = expand
        row.subRowsVisible = expand
        updateIcon(expand)
    }

    private fun updateIcon(expand: Boolean) {
        val icon = if (expand) AllIcons.General.ArrowDown else AllIcons.General.ArrowRight
        label.icon = icon
        label.disabledIcon = IconLoader.getTransparentIcon(icon, 0.5f)
    }

    init {
        cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        updateIcon(isExpanded)
        addMouseListener(object : MouseAdapter() {
            override fun mouseReleased(e: MouseEvent) {
                update(!isExpanded)
            }
        })
    }
}