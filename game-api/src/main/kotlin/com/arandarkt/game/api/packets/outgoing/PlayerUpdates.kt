package com.arandarkt.game.api.packets.outgoing

import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.entity.components.player.PlayerMaskComponent
import com.arandarkt.game.api.entity.components.player.ViewportComponent
import com.arandarkt.game.api.entity.components.player.MovementComponent
import com.arandarkt.game.api.koin.inject
import com.arandarkt.game.api.packets.GamePacketEncoder
import com.arandarkt.game.api.packets.PacketHeader
import com.arandarkt.game.api.world.location.components.PositionComponent
import com.arandarkt.game.api.world.map.IRegionManager
import io.guthix.buffer.BitBuf
import io.guthix.buffer.toBitMode
import io.netty.buffer.ByteBuf

class PlayerUpdates(val player: PlayerCharacter) {
    companion object : GamePacketEncoder<PlayerUpdates> {
        override val opcode: Int = 90
        override val header: PacketHeader = PacketHeader.SHORT

        private val regionManager: IRegionManager by inject()

        override fun encode(writer: ByteBuf, data: PlayerUpdates) {
            val player = data.player
            val bits = writer.toBitMode()
            bits.updatePlayerLocation(player)
            bits.updateLocalPlayerLocations(player)
            bits.updateLocalPlayers(player)
            writer.updateFlags(player)
        }

        private fun ByteBuf.updateFlags(player: PlayerCharacter) {
        }

        private fun BitBuf.updateLocalPlayers(player: PlayerCharacter) {
            val viewport = player.component<ViewportComponent>()
            for((index, plr) in regionManager.getLocalPlayers(player, 15).withIndex()) {
                if(plr === player || !plr.session.isConnected() || viewport.localPlayers.contains(plr))
                    continue
                if(viewport.localPlayers.size >= 255 || index == 10)
                    break
                addLocalPlayer(player, plr)
            }
            writeBits(11, 2047)
        }

        private fun BitBuf.addLocalPlayer(me: PlayerCharacter, other: PlayerCharacter) {
            val walking = other.component<MovementComponent>()
            val myViewport = me.component<ViewportComponent>()
            val otherPosition = other.component<PositionComponent>()
            val myPosition = me.component<PositionComponent>()
            writeBits(11, other.index)
            var offsetX: Int = otherPosition.x - myPosition.x
            var offsetY: Int = otherPosition.y - myPosition.y
            if (offsetY < 0) {
                offsetY += 32
            }
            if (offsetX < 0) {
                offsetX += 32
            }
            writeBits(5, offsetY)
            writeBits(3, walking.direction.ordinal)
            writeBits(1, 1)
            writeBits(1, 1)
            writeBits(5, offsetX)
            myViewport.localPlayers.add(other)
        }

        private fun BitBuf.updateLocalPlayerLocations(player: PlayerCharacter) {
            val viewport = player.component<ViewportComponent>()
            writeBits(8, viewport.localPlayers.size)
            val iterator = viewport.localPlayers.listIterator()
            while (iterator.hasNext()) {
                val other = iterator.next()
                val otherPosition = other.component<PositionComponent>()
                val otherMasks = other.component<PlayerMaskComponent>()
                if (!other.session.isConnected() || !otherPosition.withinDistance(
                        player.component(),
                        15
                    ) || otherMasks.isTeleporting
                ) {
                    writeBoolean(true)
                    writeBits(2, 3)
                    iterator.remove()
                    continue
                }
                updatePlayerDirection(other)
            }
        }

        private fun BitBuf.updatePlayerLocation(player: PlayerCharacter) {
            val masks = player.component<PlayerMaskComponent>()
            val position = player.component<PositionComponent>()
            if (masks.shouldUpdateSceneGraph || masks.isTeleporting) {
                writeBoolean(true)
                writeBits(2, 3)
                writeBits(2, position.z)
                writeBits(7, position.getSceneX(masks.lastSceneGraph))
                writeBoolean(masks.isTeleporting)
                writeBits(7, position.getSceneY(masks.lastSceneGraph))
                writeBoolean(masks.isUpdateRequired)
            } else {
                updatePlayerDirection(player)
            }
        }

        private fun BitBuf.updatePlayerDirection(player: PlayerCharacter) {
            val walking = player.component<MovementComponent>()
            val masks = player.component<PlayerMaskComponent>()
            if (walking.runningDirection != -1) {
                writeBoolean(true)
                writeBits(2, 2)
                writeBits(3, walking.walkingDirection)
                writeBits(3, walking.runningDirection)
                writeBoolean(masks.isUpdateRequired)
            } else if (walking.walkingDirection != -1) {
                writeBoolean(true)
                writeBits(2, 1)
                writeBits(3, walking.walkingDirection)
                writeBoolean(masks.isUpdateRequired)
            } else if (masks.isUpdateRequired) {
                writeBoolean(true)
                writeBits(2, 0)
            } else {
                writeBoolean(false)
            }
        }
    }
}