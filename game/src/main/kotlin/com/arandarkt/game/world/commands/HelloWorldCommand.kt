package com.arandarkt.game.world.commands

import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.packets.GameSession.Companion.sendPacket
import com.arandarkt.game.api.packets.outgoing.GameMessage
import com.arandarkt.game.api.world.commands.CommandComponent

class HelloWorldCommand : CommandComponent {
    override val name: String = "hey"
    override fun onCommand(player: PlayerCharacter, name: String, vararg args: String) {
        player.session.sendPacket(GameMessage("Hello, World"))
    }
}