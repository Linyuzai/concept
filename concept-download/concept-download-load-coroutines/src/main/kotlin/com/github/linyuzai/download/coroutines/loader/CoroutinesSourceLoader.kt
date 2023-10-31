package com.github.linyuzai.download.coroutines.loader

import com.github.linyuzai.download.core.context.DownloadContext
import com.github.linyuzai.download.core.load.ConcurrentSourceLoader
import com.github.linyuzai.download.core.load.SourceLoader
import com.github.linyuzai.download.core.source.Source
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

/**
 * 基于协程调度的 [SourceLoader]。
 */
open class CoroutinesSourceLoader : ConcurrentSourceLoader() {

    override fun concurrentLoad(sources: Collection<Source>, context: DownloadContext) {
        val results = mutableListOf<Source>()
        runBlocking {
            val deferredList = mutableListOf<Deferred<Source>>()
            sources.forEach {
                val deferred = async(Dispatchers.IO) {
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
}