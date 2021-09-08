package com.arandarkt.game.api.packets.outgoing

import com.arandarkt.game.api.packets.GamePacketEncoder
import com.arandarkt.game.api.packets.PacketHeader
import io.guthix.buffer.writeShortAddLE
import io.netty.buffer.ByteBuf

class OpenWidgetFramePacket(val frameId: Int) {
    companion object : GamePacketEncoder<OpenWidgetFramePacket> {
        override val opcode: Int = 77
        override val header: PacketHeader = PacketHeader.NORMAL
        override fun encode(writer: ByteBuf, data: OpenWidgetFramePacket) {
            writer.writeShortAddLE(data.frameId)
        }
    }
}