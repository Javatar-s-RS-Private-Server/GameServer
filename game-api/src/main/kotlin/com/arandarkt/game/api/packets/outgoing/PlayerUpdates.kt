package com.arandarkt.game.api.packets.outgoing

import com.arandarkt.game.api.components.entity.player.MovementComponent
import com.arandarkt.game.api.components.entity.player.UpdateMasksComponent
import com.arandarkt.game.api.components.entity.player.ViewportComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.koin.inject
import com.arandarkt.game.api.packets.GamePacketEncoder
import com.arandarkt.game.api.packets.GameSession
import com.arandarkt.game.api.packets.PacketHeader
import com.arandarkt.game.api.world.location.components.Position
import com.arandarkt.game.api.world.map.GameRegionManager
import io.guthix.buffer.BitBuf
import io.guthix.buffer.toBitMode
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled

class PlayerUpdates(val player: PlayerCharacter) {
    companion object : GamePacketEncoder<PlayerUpdates> {
        override val opcode: Int = 74
        override val header: PacketHeader = PacketHeader.SHORT

        private val regionManager: GameRegionManager by inject()

        override fun encode(writer: ByteBuf, data: PlayerUpdates) {
            val player = data.player
            val flagBuff = Unpooled.buffer()
            with(writer.toBitMode()) {
                writeLocationUpdate(player, flagBuff)
                writeLocalPlayers(player, flagBuff)
                addLocalPlayers(player, flagBuff)
                if(flagBuff.writerIndex() > 0) {
                    writeBits(2047, 11)
                    toByteMode().writeBytes(flagBuff)
                }
            }
        }

        private fun BitBuf.addLocalPlayers(player: PlayerCharacter, flagBuff: ByteBuf) {
            val myViewport = player.component<ViewportComponent>()
            val localPlayers = regionManager.getLocalPlayers(player, 15)
            var addCount = 0
            for (localPlayer in localPlayers) {
                val localSession = localPlayer.component<GameSession>()
                if(localPlayer === player || !localSession.isConnected() || myViewport.localPlayers.contains(localPlayer)) {
                    continue
                }
                if(myViewport.localPlayers.size >= 255 || ++addCount > 10) {
                    break
                }
                addLocalPlayer(player, localPlayer, flagBuff)
            }
        }

        private fun BitBuf.addLocalPlayer(me: PlayerCharacter, local: PlayerCharacter, flagBuff: ByteBuf) {
            val myView = me.component<ViewportComponent>()
            val myPos = me.component<Position>()
            val localPos = local.component<Position>()
            val localMov = local.component<MovementComponent>()
            val localMasks = local.component<UpdateMasksComponent>()
            writeBits(local.index, 11)
            var offsetX: Int = localPos.x - myPos.x
            var offsetY: Int = localPos.y - myPos.y
            if (offsetY < 0) {
                offsetY += 32
            }
            if (offsetX < 0) {
                offsetX += 32
            }
            writeBits(offsetX, 5)
            writeBits(offsetY, 5)
            writeBits(localMov.direction.ordinal, 3)
            writeBoolean(true)
            writeBoolean(true)

            myView.localPlayers.add(local)

            if(localMasks.isUpdateRequired) {
                flagBuff.writeFlags(local)
            }
        }

        private fun BitBuf.writeLocalPlayers(player: PlayerCharacter, flagBuff: ByteBuf) {
            val viewport = player.component<ViewportComponent>()
            writeBits(viewport.localPlayers.size, 8)
            val iterator = viewport.localPlayers.iterator()
            while(iterator.hasNext()) {
                val localPlayer = iterator.next()
                val localMov = localPlayer.component<MovementComponent>()
                val localPos = localPlayer.component<Position>()
                val session = localPlayer.component<GameSession>()
                if(!session.isConnected() || !localPos.withinDistance(player.component(), 15) || localMov.isTeleporting) {
                    writeBoolean(true)
                    writeBits(3, 2)
                    iterator.remove()
                } else {
                    writeWalkingLocation(localPlayer, flagBuff)
                }
            }
        }

        private fun BitBuf.writeLocationUpdate(player: PlayerCharacter, flagBuff: ByteBuf) {
            val masks = player.component<UpdateMasksComponent>()
            val mov = player.component<MovementComponent>()
            val pos = player.component<Position>()
            val lpos = masks.lastSceneGraph
            if(masks.shouldUpdateSceneGraph || mov.isTeleporting) {
                writeBoolean(true)
                writeBits(3, 2)
                writeBits(pos.getSceneY(lpos), 7)
                writeBits(pos.getSceneX(lpos), 7)
                writeBoolean(masks.isUpdateRequired)
                writeBits(pos.z, 2)
                writeBoolean(mov.isTeleporting)
                if (masks.isUpdateRequired) {
                    flagBuff.writeFlags(player)
                }
            } else {
                writeWalkingLocation(player, flagBuff)
            }
        }

        private fun BitBuf.writeWalkingLocation(player: PlayerCharacter, flagBuff: ByteBuf) {
            val mov = player.component<MovementComponent>()
            val masks = player.component<UpdateMasksComponent>()
            if (mov.runningDirection != -1) {
                writeBoolean(true)
                writeBits(2, 2)
                writeBits(mov.walkingDirection, 3)
                writeBits(mov.runningDirection, 3)
                writeBoolean(masks.isUpdateRequired)
                if(masks.isUpdateRequired) {
                    flagBuff.writeFlags(player)
                }
            } else if(mov.walkingDirection != -1) {
                writeBoolean(true)
                writeBits(1, 2)
                writeBits(mov.walkingDirection, 3)
                writeBoolean(masks.isUpdateRequired)
                if(masks.isUpdateRequired) {
                    flagBuff.writeFlags(player)
                }
            } else if(masks.isUpdateRequired) {
                writeBoolean(true)
                writeBits(0, 2)
                flagBuff.writeFlags(player)
            } else {
                writeBoolean(false)
            }
        }

        private fun ByteBuf.writeFlags(player: PlayerCharacter) {
            val masks = player.component<UpdateMasksComponent>()
            if (masks.maskData > 0x100) {
                masks.markAsPlayerMask()
                writeByte(masks.maskData)
                writeByte(masks.maskData shr 8)
            } else {
                writeByte(masks.maskData)
            }

            for (key in masks.flags.keys) {
                val value = masks.flags[key]!!
                with(value) { writeFlag() }
            }
            masks.flags.clear()
        }
    }
}