package com.arandarkt.game.world.commands.widget

import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.world.commands.CommandComponent

class OpenWidgetCommand : CommandComponent {
    override val name: String = "ow"
    override fun onCommand(player: PlayerCharacter, name: String, vararg args: String) {
        val child = args[0].toInt()
        val widget = args[1].toInt()
        val overlay = args.size == 3
        player.widgetManager.open(child, widget, overlay)
    }
}