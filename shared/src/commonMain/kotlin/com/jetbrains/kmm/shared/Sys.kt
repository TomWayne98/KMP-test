package com.jetbrains.kmm.shared

import com.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class Sys: CoroutineScope {
    private lateinit var childCoroutinesJob: Job
    override val coroutineContext get() = Dispatchers.Default + childCoroutinesJob


    private val dummyChanel: BroadcastChannel<String> = ConflatedBroadcastChannel()

    fun getChanel() = dummyChanel

    fun start() {
        childCoroutinesJob = Job()
        launch {
            emitValuePeriodically()
        }
    }

    fun stop() {
        childCoroutinesJob.cancel()
        launch {
            dummyChanel.cancel()
        }
    }

    @ExperimentalCoroutinesApi
    private suspend fun emitValuePeriodically() {
        KMMTimer("randomEmmiter", 2000, 0) {
            Napier.d("EMIT called", tag = "TOMW")
            // Lets freeze it for 10 secs so we can check that UI thread on client side is still running
            runBlocking {
                delay(10000)
                dummyChanel.send("Hey it is me, Kotlin Multiplatform package")
            }
        }.start()
    }
}
