/*
package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.openapi.observable.properties.*

class ConceptGraphPropertyImpl<T>(private val propertyGraph: PropertyGraph, initial: () -> T)
    : ConceptGraphProperty<T>, AtomicLazyProperty<T>(initial) {

    override fun dependsOn(parent: ObservableClearableProperty<*>) {
        propertyGraph.dependsOn(this, parent)
    }

    override fun dependsOn(parent: ObservableClearableProperty<*>, default: () -> T) {
        propertyGraph.dependsOn(this, parent, default)
    }

    override fun afterPropagation(listener: () -> Unit) {
        propertyGraph.afterPropagation(listener)
    }

    init {
        propertyGraph.register(this)
    }

    companion object {
        fun <T> PropertyGraph.graphProperty(initial: () -> T): ConceptGraphProperty<T> = ConceptGraphPropertyImpl(this, initial)

        fun <S, T> PropertyGraph.graphPropertyView(initial: () -> T, map: (S) -> T, comap: (T) -> S): GraphProperty<T> =
            this.graphProperty { comap(initial()) }.transform(map, comap)
    }

    fun <T> GraphProperty<T>.map(transform: (T) -> T) = transform(transform, { it })
    fun <T> GraphProperty<T>.comap(transform: (T) -> T) = transform({ it }, transform)
    fun <S, T> GraphProperty<S>.transform(map: (S) -> T, comap: (T) -> S): GraphProperty<T> =
        GraphPropertyView(this, map, comap)
}*/
