package com.arandarkt.game.api.packets.incoming.walking

import com.arandarkt.game.api.components.entity.player.MovementComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.packets.GameSession.Companion.sendPacket
import com.arandarkt.game.api.packets.PacketHandler
import com.arandarkt.game.api.packets.outgoing.ClearMinimapFlag

class MovementHandler(val player: PlayerCharacter) : PacketHandler<Movement> {
    override fun handlePacket(packet: Movement) {
        val mov = player.component<MovementComponent>()
        val widgetManager = player.widgetManager

        if(mov.isFrozen || mov.isLocked) {
            player.session.sendPacket(ClearMinimapFlag())
            return
        }
        widgetManager.close()



    }
}