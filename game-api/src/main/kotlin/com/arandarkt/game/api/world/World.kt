package com.arandarkt.game.api.world

import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.collection.CharacterList
import com.arandarkt.game.api.entity.collection.EntityList

interface World {

    val players: CharacterList<PlayerCharacter>
    val npcs: EntityList<out Character>

    val loginQueue: ArrayDeque<PlayerCharacter>
    val logoutQueue: ArrayDeque<PlayerCharacter>

    fun queueLogin(player: PlayerCharacter)
    fun queueLogout(player: PlayerCharacter)

    fun addPlayer(player: PlayerCharacter)
    fun removePlayer(player: PlayerCharacter)

}