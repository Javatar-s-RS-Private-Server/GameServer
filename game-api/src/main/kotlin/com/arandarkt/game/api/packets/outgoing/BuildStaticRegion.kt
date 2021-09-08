package com.arandarkt.game.api.packets.outgoing

import com.arandarkt.game.api.packets.GamePacketEncoder
import com.arandarkt.game.api.packets.PacketHeader
import io.guthix.buffer.writeShortAdd
import io.netty.buffer.ByteBuf

class BuildStaticRegion(val regionX: Int, val regionY: Int, val sceneX: Int, val sceneY: Int, val z: Int) {
    companion object : GamePacketEncoder<BuildStaticRegion> {
        override val opcode: Int = 221
        override val header: PacketHeader = PacketHeader.SHORT
        override fun encode(writer: ByteBuf, data: BuildStaticRegion) {
            writer.writeShortAdd(data.regionX)
            for (regionX in (data.regionX - 6) / 8..(data.regionX + 6) / 8) {
                for (regionY in (data.regionY - 6) / 8..(data.regionY + 6) / 8) {
                    repeat(4) { writer.writeIntLE(0) }
                }
            }
            writer.writeShortLE(data.regionY)
            writer.writeShort(data.sceneX)
            writer.writeByte(data.z)
            writer.writeShort(data.sceneY)
        }
    }
}