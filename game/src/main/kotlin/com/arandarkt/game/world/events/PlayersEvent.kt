package com.arandarkt.game.world.events

import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.entity.hasComponent
import com.arandarkt.game.api.koin.inject
import com.arandarkt.game.api.packets.GameSession
import com.arandarkt.game.api.world.World
import com.arandarkt.game.api.world.WorldEvent

class PlayersEvent : WorldEvent {
    val world: World by inject()
    override suspend fun onTick(currentTick: Long) {
        world.players.forEach {
            if(it != null && it.hasComponent<GameSession>()) {

                if(it.component<GameSession>().isConnected()) {
                    it.onTick(currentTick)
                } else {
                    world.queueLogout(it)
                }

            }
        }
    }
}