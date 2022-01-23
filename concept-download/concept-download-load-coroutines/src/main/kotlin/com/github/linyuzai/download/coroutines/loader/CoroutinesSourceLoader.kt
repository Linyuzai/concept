package com.github.linyuzai.download.coroutines.loader

import com.github.linyuzai.download.core.context.DownloadContext
import com.github.linyuzai.download.core.load.ConcurrentSourceLoader
import com.github.linyuzai.download.core.source.Source
import com.github.linyuzai.download.core.source.multiple.MultipleSource
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import reactor.core.publisher.Mono

open class CoroutinesSourceLoader : ConcurrentSourceLoader() {

    override fun concurrentLoad(sources: Collection<Source>, context: DownloadContext): Mono<Source> {
        val results = mutableListOf<Source>()
        runBlocking {
            val deferredList = mutableListOf<Deferred<Source>>()
            sources.forEach {
                val deferred = async(Dispatchers.IO) {
                    it.load(context).block()
                    return@async it
                }
                deferredList.add(deferred)
            }
            deferredList.forEach {
                it.await()
            }
        }
        return Mono.just(MultipleSource(results))
    }
}