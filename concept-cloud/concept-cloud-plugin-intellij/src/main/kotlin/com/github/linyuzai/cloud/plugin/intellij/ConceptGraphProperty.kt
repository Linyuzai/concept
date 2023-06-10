package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.openapi.Disposable
import com.intellij.openapi.observable.properties.*
import com.intellij.util.containers.DisposableWrapperList
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KProperty

interface ConceptGraphProperty<T> {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = get()

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = set(value)

    fun get(): T

    fun set(value: T)

    fun reset()

    fun afterChange(listener: (T) -> Unit)

    fun afterReset(listener: () -> Unit)

    fun afterChange(listener: (T) -> Unit, parentDisposable: Disposable)

    fun afterReset(listener: () -> Unit, parentDisposable: Disposable)

    fun dependsOn(parent: ConceptGraphProperty<*>)

    fun dependsOn(parent: ConceptGraphProperty<*>, default: () -> T)

    fun afterPropagation(listener: () -> Unit)

    fun updateAndGet(update: (T) -> T): T
}

class ConceptGraphPropertyImpl<T>(private val propertyGraph: ConceptPropertyGraph, private val initial: () -> T) :
    ConceptGraphProperty<T> {

    private val changeListeners1 = CopyOnWriteArrayList<(T) -> Unit>()
    private val resetListeners1 = CopyOnWriteArrayList<() -> Unit>()

    private val changeListeners2 = DisposableWrapperList<(T) -> Unit>()
    private val resetListeners2 = DisposableWrapperList<() -> Unit>()

    init {
        propertyGraph.register(this)
    }

    override fun dependsOn(parent: ConceptGraphProperty<*>) {
        propertyGraph.dependsOn(this, parent)
    }

    override fun dependsOn(parent: ConceptGraphProperty<*>, default: () -> T) {
        propertyGraph.dependsOn(this, parent, default)
    }

    override fun afterPropagation(listener: () -> Unit) {
        propertyGraph.afterPropagation(listener)
    }

    protected fun fireChangeEvent(value: T) {
        changeListeners1.forEach { it(value) }
    }

    protected fun fireResetEvent() {
        resetListeners1.forEach { it() }
    }

    override fun afterChange(listener: (T) -> Unit) {
        changeListeners1.add(listener)
    }

    override fun afterReset(listener: () -> Unit) {
        resetListeners1.add(listener)
    }

    override fun afterChange(listener: (T) -> Unit, parentDisposable: Disposable) {
        changeListeners2.add(listener, parentDisposable)
    }

    override fun afterReset(listener: () -> Unit, parentDisposable: Disposable) {
        resetListeners2.add(listener, parentDisposable)
    }

    private val value = AtomicReference(UNINITIALIZED_VALUE)

    override fun get(): T {
        return update { it }
    }

    override fun set(value: T) {
        this.value.set(value)
        fireChangeEvent(value)
    }

    override fun updateAndGet(update: (T) -> T): T {
        val newValue = update(update)
        fireChangeEvent(newValue)
        return newValue
    }

    @Suppress("UNCHECKED_CAST")
    private fun update(update: (T) -> T): T {
        val resolve = { it: Any? -> if (it === UNINITIALIZED_VALUE) initial() else it as T }
        return value.updateAndGet { update(resolve(it)) } as T
    }

    override fun reset() {
        value.set(UNINITIALIZED_VALUE)
        fireResetEvent()
    }

    companion object {
        private val UNINITIALIZED_VALUE = Any()
    }
}

private class ConceptGraphPropertyView<S, T>(
    private val instance: ConceptGraphProperty<S>,
    private val map: (S) -> T,
    private val comap: (T) -> S
) : ConceptGraphProperty<T> {
    override fun dependsOn(parent: ConceptGraphProperty<*>) =
        instance.dependsOn(parent)

    override fun dependsOn(parent: ConceptGraphProperty<*>, default: () -> T) =
        instance.dependsOn(parent) { comap(default()) }

    override fun afterPropagation(listener: () -> Unit) =
        instance.afterPropagation(listener)

    override fun updateAndGet(update: (T) -> T): T {
        throw UnsupportedOperationException()
    }

    override fun get(): T =
        map(instance.get())

    override fun set(value: T) =
        instance.set(comap(value))

    override fun reset() =
        instance.reset()

    override fun afterChange(listener: (T) -> Unit) =
        instance.afterChange { listener(map(it)) }

    override fun afterReset(listener: () -> Unit) =
        instance.afterReset(listener)

    override fun afterChange(listener: (T) -> Unit, parentDisposable: Disposable) =
        instance.afterChange({ listener(map(it)) }, parentDisposable)

    override fun afterReset(listener: () -> Unit, parentDisposable: Disposable) =
        instance.afterReset(listener, parentDisposable)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return instance == other
    }

    override fun hashCode(): Int {
        return instance.hashCode()
    }
}

fun <T> ConceptGraphProperty<T>.map(transform: (T) -> T) = transform(transform, { it })
fun <T> ConceptGraphProperty<T>.comap(transform: (T) -> T) = transform({ it }, transform)
fun <S, T> ConceptGraphProperty<S>.transform(map: (S) -> T, comap: (T) -> S): ConceptGraphProperty<T> =
    ConceptGraphPropertyView(this, map, comap)


fun <T> ConceptPropertyGraph.property(initial: () -> T): ConceptGraphProperty<T> = ConceptGraphPropertyImpl(this, initial)

fun <S, T> ConceptPropertyGraph.graphPropertyView(initial: () -> T, map: (S) -> T, comap: (T) -> S): ConceptGraphProperty<T> =
    this.property { comap(initial()) }.transform(map, comap)
