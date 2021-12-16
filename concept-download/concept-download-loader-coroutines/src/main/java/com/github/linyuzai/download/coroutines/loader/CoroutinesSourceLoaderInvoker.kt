package com.github.linyuzai.download.coroutines.loader

import com.github.linyuzai.download.core.context.DownloadContext
import com.github.linyuzai.download.core.load.LoadResult
import com.github.linyuzai.download.core.load.ParallelSourceLoaderInvoker
import com.github.linyuzai.download.core.load.SourceLoader
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class CoroutinesSourceLoaderInvoker : ParallelSourceLoaderInvoker() {

    override fun parallelInvoke(loaders: Collection<SourceLoader>, context: DownloadContext): Collection<LoadResult> {
        val results = mutableListOf<LoadResult>()
        runBlocking {
            val deferredList = mutableListOf<Deferred<LoadResult>>()
            loaders.forEach {
                val deferred = async(Dispatchers.IO) {
                    it.load(context)
                }
                deferredList.add(deferred)
            }
            deferredList.forEach {
                results.add(it.await())
            }
        }
        return results
    }
}