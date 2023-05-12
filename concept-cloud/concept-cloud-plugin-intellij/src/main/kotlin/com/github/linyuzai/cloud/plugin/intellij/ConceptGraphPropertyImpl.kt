/*
package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.openapi.observable.properties.AtomicLazyProperty
import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.observable.properties.ObservableProperty
import com.intellij.openapi.observable.properties.PropertyGraph

import com.intellij.openapi.Disposable
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
@Suppress("DEPRECATION")
class ConceptGraphPropertyImpl<T>(private val propertyGraph: ConceptPropertyGraph, initial: () -> T)
    : GraphProperty<T>, AtomicLazyProperty<T>(initial) {

    override fun dependsOn(parent: ObservableProperty<*>, update: () -> T) {
        propertyGraph.dependsOn(this, parent, update)
    }

    override fun afterPropagation(listener: () -> Unit) {
        propertyGraph.afterPropagation(listener = listener)
    }

    override fun afterPropagation(parentDisposable: Disposable?, listener: () -> Unit) {
        propertyGraph.afterPropagation(parentDisposable, listener)
    }

    init {
        propertyGraph.register(this)
    }

    @Deprecated("Use set instead")
    @ApiStatus.ScheduledForRemoval
    override fun reset() =
        super<AtomicLazyProperty>.reset()

    @Deprecated("Use afterChange instead")
    @ApiStatus.ScheduledForRemoval
    override fun afterReset(listener: () -> Unit) =
        super<AtomicLazyProperty>.afterReset(listener)

    @Deprecated("Use afterChange instead")
    @ApiStatus.ScheduledForRemoval
    override fun afterReset(listener: () -> Unit, parentDisposable: Disposable) =
        super<AtomicLazyProperty>.afterReset(listener, parentDisposable)

    companion object {
        @Deprecated("Please use PropertyGraph.property instead", ReplaceWith("property(initial)"))
        @ApiStatus.ScheduledForRemoval
        fun <T> PropertyGraph.graphProperty(initial: T): GraphProperty<T> = property(initial)

        @Deprecated("Please use PropertyGraph.lazyProperty instead", ReplaceWith("lazyProperty(initial)"))
        @ApiStatus.ScheduledForRemoval
        fun <T> PropertyGraph.graphProperty(initial: () -> T): GraphProperty<T> = lazyProperty(initial)
    }
}*/
