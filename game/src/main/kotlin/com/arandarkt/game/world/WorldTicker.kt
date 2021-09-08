package com.arandarkt.game.world

import com.arandarkt.game.api.world.Ticker
import com.arandarkt.game.api.world.WorldEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class WorldTicker : Ticker {

    private val events = mutableListOf<Job>()

    override val ticker = flow {
        val duration = 600L
        var lastTick = 0L
        while (true) {
            val currentTick = if (lastTick < 0) duration else lastTick
            delay(currentTick)
            emit(currentTick)
            lastTick = duration - (System.currentTimeMillis() - lastTick)
        }
    }

    override fun onTick(action: suspend (Long) -> Unit) {
        events.add(ticker.onEach { action(it) }.launchIn(this))
    }

    override fun onTick(event: WorldEvent) {
        onTick { event.onTick(it) }
    }

    override fun shutdown() {
        events.forEach(Job::cancel)
    }

}