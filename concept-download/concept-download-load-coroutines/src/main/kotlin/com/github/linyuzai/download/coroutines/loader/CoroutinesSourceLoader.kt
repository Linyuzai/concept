package com.github.linyuzai.download.coroutines.loader

import com.github.linyuzai.download.core.context.DownloadContext
import com.github.linyuzai.download.core.exception.ErrorHolder
import com.github.linyuzai.download.core.load.ConcurrentSourceLoader
import com.github.linyuzai.download.core.source.Source
import com.github.linyuzai.download.core.source.multiple.MultipleSource
import kotlinx.coroutines.*
import reactor.core.publisher.Mono

open class CoroutinesSourceLoader : ConcurrentSourceLoader() {

    override fun concurrentLoad(sources: Collection<Source>, context: DownloadContext): Mono<Source> {
        val results = mutableListOf<Source>()
        runBlocking {
            val deferredList = mutableListOf<Deferred<Unit>>()
            sources.forEach {
                val deferred = async(Dispatchers.IO) {
                    val holder = ErrorHolder();
                    it.load(context)
                        .subscribe({
                            results.add(it);
                        }, holder::set)
                    holder.throwIfError()
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