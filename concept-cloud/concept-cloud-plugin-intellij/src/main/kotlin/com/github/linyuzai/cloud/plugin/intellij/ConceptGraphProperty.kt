/*
package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.openapi.Disposable
import com.intellij.openapi.observable.properties.ObservableClearableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface ConceptGraphProperty<T> : ReadWriteProperty<Any?, T> {

    fun dependsOn(parent: ObservableClearableProperty<*>)

    fun dependsOn(parent: ObservableClearableProperty<*>, default: () -> T)

    fun afterPropagation(listener: () -> Unit)

    fun get(): T

    fun set(value: T)

    fun reset()

    fun afterChange(listener: (T) -> Unit)

    fun afterReset(listener: () -> Unit)

    fun afterChange(listener: (T) -> Unit, parentDisposable: Disposable)

    fun afterReset(listener: () -> Unit, parentDisposable: Disposable)

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = get()

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = set(value)
}*/
