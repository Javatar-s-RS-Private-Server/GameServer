package com.arandarkt.game.api.packets.incoming.walking

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.components.ComponentManager
import com.arandarkt.game.api.components.entity.player.MovementComponent
import com.arandarkt.game.api.components.entity.player.action.movement.MovementAction
import com.arandarkt.game.api.entity.Entity
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.packets.GameSession
import com.arandarkt.game.api.packets.GameSession.Companion.sendPacket
import com.arandarkt.game.api.packets.PacketHandler
import com.arandarkt.game.api.packets.outgoing.ClearMinimapFlag
import com.arandarkt.game.api.world.location.components.Position

class MovementHandler(val player: PlayerCharacter) : PacketHandler<Movement> {
    override fun handlePacket(packet: Movement) {
        val mov = player.component<MovementComponent>()
        val pos = player.component<Position>()
        val session = player.component<GameSession>()
        val widgetManager = player.widgetManager

        if(mov.isFrozen || mov.isLocked) {
            session.sendPacket(ClearMinimapFlag())
            return
        }

        widgetManager.close()

        mov.reset()

        mov.clearEntityFacingDirection()
        mov.clearEntityPositionDirection()

        val entity = PosEntity(Position(packet.x, packet.y, pos.z))
        player.startAction(MovementAction(entity, forceRun = mov.isRunDisabled || packet.isRunning))
    }

    private class PosEntity(pos : Position) : Entity {
        override val components: ComponentManager<Component> = ComponentManager<Component>().with(pos)
        override val worldSize: Int = 1
        override suspend fun onTick(tick: Long) {}
    }
}