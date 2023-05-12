/*
package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.ui.dsl.checkNonNegative
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.JBEmptyBorder

data class ConceptGaps(val top: Int = 0, val left: Int = 0, val bottom: Int = 0, val right: Int = 0) {
    companion object {
        @JvmField
        val EMPTY = ConceptGaps(0)
    }

    init {
        checkNonNegative("top", top)
        checkNonNegative("left", left)
        checkNonNegative("bottom", bottom)
        checkNonNegative("right", right)
    }

    constructor(size: Int) : this(size, size, size, size)

    val width: Int
        get() = left + right

    val height: Int
        get() = top + bottom
}

fun JBGaps(top: Int = 0, left: Int = 0, bottom: Int = 0, right: Int = 0): ConceptGaps {
    return ConceptGaps(JBUIScale.scale(top), JBUIScale.scale(left), JBUIScale.scale(bottom), JBUIScale.scale(right))
}

fun ConceptGaps.toJBEmptyBorder(): JBEmptyBorder {
    return JBEmptyBorder(top, left, bottom, right)
}
*/
