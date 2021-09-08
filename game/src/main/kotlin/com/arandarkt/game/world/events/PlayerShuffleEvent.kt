package com.arandarkt.game.world.events

import com.arandarkt.game.api.koin.inject
import com.arandarkt.game.api.world.World
import com.arandarkt.game.api.world.WorldEvent
import kotlinx.coroutines.delay

class PlayerShuffleEvent : WorldEvent {

    val world: World by inject()

    override suspend fun onTick(currentTick: Long) {
        delay(900000)
        if (world.players.isNotEmpty()) {
            world.players.shuffle()
        }
    }

}