package com.arandarkt.game.api.world

import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.character.player.PlayerDetails
import com.arandarkt.game.api.packets.GameSession

fun interface PlayerFactory {

    fun newPlayer(details: PlayerDetails): PlayerCharacter

}