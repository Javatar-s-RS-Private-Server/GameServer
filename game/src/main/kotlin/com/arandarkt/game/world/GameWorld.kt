package com.arandarkt.game.world

import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.collection.CharacterList
import com.arandarkt.game.api.entity.collection.EntityList
import com.arandarkt.game.api.entity.collection.EntityLists
import com.arandarkt.game.api.koin.inject
import com.arandarkt.game.api.world.World

class GameWorld : World {

    val factory: EntityLists by inject()

    override val players = factory.newPlayerList()
    override val npcs: EntityList<Character>
        get() = TODO("Not yet implemented")

    override val loginQueue: ArrayDeque<PlayerCharacter> = ArrayDeque()
    override val logoutQueue: ArrayDeque<PlayerCharacter> = ArrayDeque()

    @Synchronized
    override fun queueLogin(player: PlayerCharacter) {
        loginQueue.add(player)
    }

    @Synchronized
    override fun queueLogout(player: PlayerCharacter) {
        logoutQueue.add(player)
    }

    override fun addPlayer(player: PlayerCharacter) {
        players.addEntity(player)
    }

    override fun removePlayer(player: PlayerCharacter) {
        players.removeEntity(player)
    }

}