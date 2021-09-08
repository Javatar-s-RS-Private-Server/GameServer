package com.arandarkt.game.world.events

import com.arandarkt.game.api.koin.inject
import com.arandarkt.game.api.world.World
import com.arandarkt.game.api.world.WorldEvent

class PlayersEvent : WorldEvent {
    val world: World by inject()
    override suspend fun onTick(currentTick: Long) {
        world.players.forEach {
            if(it != null) {

                if(it.session.isConnected()) {
                    it.onTick(currentTick)
                } else {
                    world.queueLogout(it)
                }

            }
        }
    }
}