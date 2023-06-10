package com.github.linyuzai.cloud.plugin.intellij

import com.github.linyuzai.cloud.plugin.intellij.ConceptCompoundParallelOperationTrace.Companion.task
import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.Disposer
import com.intellij.util.containers.DisposableWrapperList
import org.jetbrains.annotations.NonNls
import java.util.concurrent.atomic.AtomicInteger

class ConceptAnonymousParallelOperationTrace private constructor(
    private val delegate: ConceptCompoundParallelOperationTrace<Nothing?>
) : ConceptObservableOperationTrace by delegate {
    constructor(@NonNls debugName: String? = null) : this(ConceptCompoundParallelOperationTrace<Nothing?>(debugName))

    fun startTask() = delegate.startTask(null)
    fun finishTask() = delegate.finishTask(null)

    companion object {
        fun <R> ConceptAnonymousParallelOperationTrace.task(action: () -> R): R = delegate.task(null, action)
    }
}

interface ConceptObservableOperationTrace {
    /**
     * Checks that operations is completed.
     */
    fun isOperationCompleted(): Boolean

    /**
     * Subscribes listener with TTL on operation start event.
     * Subscribed listener will be automatically unsubscribed when [ttl] is out or when [parentDisposable] is disposed.
     * [parentDisposable] uses for early unsubscription when listener is called less than [ttl] times.
     *
     * @param ttl is a number of listener calls which should be passed to unsubscribe listener.
     * @param listener is a listener function that will be called [ttl] times.
     * @param parentDisposable is a subscription disposable.
     */
    fun beforeOperation(ttl: Int, listener: () -> Unit, parentDisposable: Disposable)

    /**
     * Subscribes listener on operation start event that will never been unsubscribed.
     */
    fun beforeOperation(listener: () -> Unit)

    /**
     * Subscribes listener on operation start event that unsubscribed when [parentDisposable] is disposed.
     */
    fun beforeOperation(listener: () -> Unit, parentDisposable: Disposable)

    /**
     * Subscribes listener with TTL on operation finish event.
     *
     * @see beforeOperation(Int, () -> Unit, Disposable)
     */
    fun afterOperation(ttl: Int, listener: () -> Unit, parentDisposable: Disposable)

    /**
     * Subscribes listener on operation finish event that will never been unsubscribed.
     */
    fun afterOperation(listener: () -> Unit)

    /**
     * Subscribes listener on operation finish event that unsubscribed when [parentDisposable] is disposed.
     */
    fun afterOperation(listener: () -> Unit, parentDisposable: Disposable)
}

class ConceptCompoundParallelOperationTrace<Id>(private val debugName: String? = null) :
    ConceptAbstractObservableOperationTrace() {

    private val traces = LinkedHashMap<Id, Int>()

    override fun isOperationCompleted(): Boolean {
        synchronized(this) {
            return traces.isEmpty()
        }
    }

    fun startTask(taskId: Id) {
        val isOperationCompletedBeforeStart: Boolean
        synchronized(this) {
            isOperationCompletedBeforeStart = traces.isEmpty()
            addTask(taskId)
        }
        if (isOperationCompletedBeforeStart) {
            debug("Operation is started")
            fireOperationStarted()
        }
    }

    fun finishTask(taskId: Id) {
        val isOperationCompletedAfterFinish: Boolean
        synchronized(this) {
            if (!removeTask(taskId)) return
            isOperationCompletedAfterFinish = traces.isEmpty()
        }
        if (isOperationCompletedAfterFinish) {
            debug("Operation is finished")
            fireOperationFinished()
        }
    }

    private fun addTask(taskId: Id) {
        val taskCounter = traces.getOrPut(taskId) { 0 }
        traces[taskId] = taskCounter + 1
        debug("Task is started with id `$taskId`")
    }

    private fun removeTask(taskId: Id): Boolean {
        debug("Task is finished with id `$taskId`")
        val taskCounter = traces[taskId] ?: return false
        when (taskCounter) {
            1 -> traces.remove(taskId)
            else -> traces[taskId] = taskCounter - 1
        }
        return taskCounter == 1
    }

    private fun debug(message: String) {
        if (LOG.isDebugEnabled) {
            val debugPrefix = if (debugName == null) "" else "$debugName: "
            LOG.debug("$debugPrefix$message")
        }
    }

    companion object {
        private val LOG = Logger.getInstance(ConceptCompoundParallelOperationTrace::class.java)

        fun <Id, R> ConceptCompoundParallelOperationTrace<Id>.task(taskId: Id, action: () -> R): R {
            startTask(taskId)
            try {
                return action()
            } finally {
                finishTask(taskId)
            }
        }
    }
}

abstract class ConceptAbstractObservableOperationTrace : ConceptObservableOperationTrace {

    private val beforeOperationListeners = DisposableWrapperList<() -> Unit>()
    private val afterOperationListeners = DisposableWrapperList<() -> Unit>()

    protected fun fireOperationStarted() {
        beforeOperationListeners.forEach { it() }
    }

    protected fun fireOperationFinished() {
        afterOperationListeners.forEach { it() }
    }

    override fun beforeOperation(ttl: Int, listener: () -> Unit, parentDisposable: Disposable) {
        subscribe(ttl, listener, ::beforeOperation, parentDisposable)
    }

    override fun beforeOperation(listener: () -> Unit) {
        beforeOperationListeners.add(listener)
    }

    override fun beforeOperation(listener: () -> Unit, parentDisposable: Disposable) {
        beforeOperationListeners.add(listener, parentDisposable)
    }

    override fun afterOperation(ttl: Int, listener: () -> Unit, parentDisposable: Disposable) {
        subscribe(ttl, listener, ::afterOperation, parentDisposable)
    }

    override fun afterOperation(listener: () -> Unit) {
        afterOperationListeners.add(listener)
    }

    override fun afterOperation(listener: () -> Unit, parentDisposable: Disposable) {
        afterOperationListeners.add(listener, parentDisposable)
    }
}

@JvmName("subscribe0")
fun subscribe(
    ttl: Int,
    listener: () -> Unit,
    subscribe: (() -> Unit, Disposable) -> Unit,
    parentDisposable: Disposable
) {
    val ttlCounter = ConceptTTLCounter(ttl, parentDisposable)
    subscribe({
        ttlCounter.update()
        listener()
    }, ttlCounter)
}

private class ConceptTTLCounter(ttl: Int, parentDisposable: Disposable) : Disposable {
    private val ttlCounter = AtomicInteger(ttl)

    fun update() {
        if (ttlCounter.decrementAndGet() == 0) {
            Disposer.dispose(this)
        }
    }

    override fun dispose() {}

    init {
        require(ttl > 0)
        Disposer.register(parentDisposable, this)
    }
}
