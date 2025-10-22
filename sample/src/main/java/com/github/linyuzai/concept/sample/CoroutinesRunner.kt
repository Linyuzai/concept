package com.github.linyuzai.concept.sample

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.function.Supplier
import javax.websocket.Session

object CoroutinesRunner {

    fun run(list: Collection<Supplier<Session>>): Collection<Session> {
        val results = mutableListOf<Session>()
        runBlocking {
            val deferredList = mutableListOf<Deferred<Session>>()
            list.forEach {
                val deferred = async {
                    return@async it.get()
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