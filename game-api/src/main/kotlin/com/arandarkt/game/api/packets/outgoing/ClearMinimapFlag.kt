package com.arandarkt.game.api.packets.outgoing

import com.arandarkt.game.api.packets.GamePacketEncoder
import com.arandarkt.game.api.packets.PacketHeader
import io.netty.buffer.ByteBuf

class ClearMinimapFlag {
    companion object : GamePacketEncoder<ClearMinimapFlag> {
        override val opcode: Int = 134
        override val header: PacketHeader = PacketHeader.NORMAL
        override fun encode(writer: ByteBuf, data: ClearMinimapFlag) {}
    }
}