package com.github.linyuzai.download.coroutines.loader

import com.github.linyuzai.download.core.context.DownloadContext
import com.github.linyuzai.download.core.executor.DownloadExecutor
import com.github.linyuzai.download.core.load.ConcurrentSourceLoader
import com.github.linyuzai.download.core.load.SourceLoader
import com.github.linyuzai.download.core.source.Source
import kotlinx.coroutines.*

/**
 * 基于协程调度的 [SourceLoader]。
 */
open class CoroutinesSourceLoader : ConcurrentSourceLoader() {

    override fun concurrentLoad(sources: Collection<Source>, context: DownloadContext) {
        val results = mutableListOf<Source>()
        val dispatcher = getCoroutineDispatcher(context)
        runBlocking {
            val deferredList = mutableListOf<Deferred<Source>>()
            sources.forEach {
                val deferred = async(dispatcher) {
                    it.load(context)
                    return@async it
                }
                deferredList.add(deferred)
            }
            deferredList.forEach {
                results.add(it.await())
            }
        }
    }

    open fun getCoroutineDispatcher(context: DownloadContext): CoroutineDispatcher {
        val executor = DownloadExecutor.getExecutor(context)
        return executor?.asCoroutineDispatcher() ?: Dispatchers.IO
    }
}