package com.arandarkt.game.api.world.commands

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

interface CommandComponent : Component {

    val name: String

    fun onCommand(player: PlayerCharacter, name: String, vararg args: String)

}