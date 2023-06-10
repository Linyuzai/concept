package com.github.linyuzai.cloud.plugin.intellij

import com.github.linyuzai.cloud.plugin.intellij.ConceptAnonymousParallelOperationTrace.Companion.task
import com.intellij.openapi.util.RecursionManager
import org.jetbrains.annotations.TestOnly
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class ConceptPropertyGraph(debugName: String? = null, private val isBlockPropagation: Boolean = true) {

    private val propagation = ConceptAnonymousParallelOperationTrace((if (debugName == null) "" else " of $debugName") + ": Graph propagation")
    private val properties = ConcurrentHashMap<ConceptGraphProperty<*>, PropertyNode>()
    private val dependencies = ConcurrentHashMap<PropertyNode, CopyOnWriteArrayList<Dependency<*>>>()
    private val recursionGuard = RecursionManager.createGuard<PropertyNode>(ConceptPropertyGraph::class.java.name)

    fun <T> dependsOn(child: ConceptGraphProperty<T>, parent: ConceptGraphProperty<*>) {
        addDependency(child, parent) { reset() }
    }

    fun <T> dependsOn(child: ConceptGraphProperty<T>, parent: ConceptGraphProperty<*>, update: () -> T) {
        addDependency(child, parent) { updateAndGet { update() } }
    }

    private fun <T> addDependency(child: ConceptGraphProperty<T>, parent: ConceptGraphProperty<*>, update: ConceptGraphProperty<T>.() -> Unit) {
        val childNode = properties[child] ?: throw IllegalArgumentException("Unregistered child property")
        val parentNode = properties[parent] ?: throw IllegalArgumentException("Unregistered parent property")
        dependencies.putIfAbsent(parentNode, CopyOnWriteArrayList())
        val children = dependencies.getValue(parentNode)
        children.add(Dependency(childNode, child, update))
    }

    fun afterPropagation(listener: () -> Unit) {
        propagation.afterOperation(listener)
    }

    fun register(property: ConceptGraphProperty<*>) {
        val node = PropertyNode()
        properties[property] = node
        property.afterChange {
            recursionGuard.doPreventingRecursion(node, false) {
                propagation.task {
                    node.isPropagationBlocked = isBlockPropagation
                    propagateChange(node)
                }
            }
        }
        property.afterReset {
            node.isPropagationBlocked = false
        }
    }

    private fun propagateChange(parent: PropertyNode) {
        val dependencies = dependencies[parent] ?: return
        for (dependency in dependencies) {
            val child = dependency.node
            if (child.isPropagationBlocked) continue
            recursionGuard.doPreventingRecursion(child, false) {
                dependency.applyUpdate()
                propagateChange(child)
            }
        }
    }

    @TestOnly
    fun isPropagationBlocked(property: ConceptGraphProperty<*>) =
        properties.getValue(property).isPropagationBlocked

    private inner class PropertyNode {
        @Volatile
        var isPropagationBlocked = false
    }

    private data class Dependency<T>(
        val node: PropertyNode,
        val property: ConceptGraphProperty<T>,
        val update: ConceptGraphProperty<T>.() -> Unit
    ) {
        fun applyUpdate() = property.update()
    }
}