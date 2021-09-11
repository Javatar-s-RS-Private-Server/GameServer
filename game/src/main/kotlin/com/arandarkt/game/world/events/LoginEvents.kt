package com.arandarkt.game.world.events

import com.arandarkt.game.api.koin.inject
import com.arandarkt.game.api.world.World
import com.arandarkt.game.api.world.WorldEvent

class LoginEvents : WorldEvent {

    val world: World by inject()

    override suspend fun onTick(currentTick: Long) {

        val logins = world.loginQueue.take(40)

        logins.forEach {
            world.players.addEntity(it)
            it.initialize()
        }

        world.loginQueue.removeAll(logins)

        val logouts = world.logoutQueue.take(40)

        logouts.forEach {
            it.logout()
            world.players.removeEntity(it)
        }

        world.logoutQueue.removeAll(logouts)

    }
}