package com.arandarkt.game.api.world

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

interface Ticker : CoroutineScope {

    val ticker: Flow<Long>

    fun onTick(action: suspend (Long) -> Unit )
    fun onTick(event: WorldEvent)

    fun shutdown()

    override val coroutineContext: CoroutineContext
        get() = gameDispatcher

    companion object {
        val gameDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

        val GAME get() = CoroutineScope(gameDispatcher)

        suspend fun delayTicks(ticks: Long) {
            delay((600 * ticks))
        }
    }

}