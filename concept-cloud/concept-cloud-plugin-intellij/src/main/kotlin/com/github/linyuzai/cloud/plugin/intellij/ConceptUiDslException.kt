/*
package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.diagnostic.logger
import com.intellij.ui.dsl.gridLayout.Constraints
import org.jetbrains.annotations.ApiStatus
import java.awt.Component
import javax.swing.JComponent

class ConceptUiDslException(message: String = "Internal error", cause: Throwable? = null) : RuntimeException(message, cause) {

    companion object {
        fun error(message: String) {
            if (PluginManagerCore.isRunningFromSources()) {
                throw ConceptUiDslException(message)
            }
            else {
                logger<ConceptUiDslException>().error(message)
            }
        }
    }
}

fun checkTrue(value: Boolean) {
    if (!value) {
        throw ConceptUiDslException()
    }
}

fun checkNonNegative(name: String, value: Int) {
    if (value < 0) {
        throw ConceptUiDslException("Value cannot be negative: $name = $value")
    }
}

fun checkPositive(name: String, value: Int) {
    if (value <= 0) {
        throw ConceptUiDslException("Value must be positive: $name = $value")
    }
}

fun checkComponent(component: Component?): JComponent {
    if (component !is JComponent) {
        throw ConceptUiDslException("Only JComponents are supported: $component")
    }

    return component
}

fun checkConstraints(constraints: Any?): ConceptConstraints {
    if (constraints !is ConceptConstraints) {
        throw ConceptUiDslException("Invalid constraints: $constraints")
    }

    return constraints
}
*/
