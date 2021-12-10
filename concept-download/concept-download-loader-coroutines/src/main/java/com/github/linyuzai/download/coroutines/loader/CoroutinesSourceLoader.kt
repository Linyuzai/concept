package com.github.linyuzai.download.coroutines.loader

import com.github.linyuzai.download.core.context.DownloadContext
import com.github.linyuzai.download.core.loader.ParallelSourceLoader
import com.github.linyuzai.download.core.source.Source
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class CoroutinesSourceLoader : ParallelSourceLoader() {

    override fun parallelLoad(sources: Collection<Source>, context: DownloadContext) {
        runBlocking {
            val deferredList = mutableListOf<Deferred<Unit>>()
            sources.forEach {
                val deferred = async(Dispatchers.IO) {
                    it.load(context)
                }
                deferredList.add(deferred)
            }
            deferredList.forEach { it.await() }
        }
    }
}