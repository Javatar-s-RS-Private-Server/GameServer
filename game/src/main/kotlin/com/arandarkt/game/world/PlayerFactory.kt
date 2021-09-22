package com.arandarkt.game.world

import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.character.player.PlayerDetails
import com.arandarkt.game.api.packets.GameSession
import com.arandarkt.game.api.world.PlayerFactory
import com.arandarkt.game.entity.character.player.Player

class PlayerFactory : PlayerFactory {
    override fun newPlayer(details: PlayerDetails): PlayerCharacter {
        return Player(details)
    }
}