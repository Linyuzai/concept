package com.github.linyuzai.download.coroutines.loader

import com.github.linyuzai.download.core.context.DownloadContext
import com.github.linyuzai.download.core.loader.ParallelOriginalSourceLoader
import com.github.linyuzai.download.core.original.OriginalSource
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class CoroutinesOriginalSourceLoader : ParallelOriginalSourceLoader() {

    override fun parallelLoad(sources: Collection<OriginalSource>, context: DownloadContext) {
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