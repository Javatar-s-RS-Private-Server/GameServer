package com.arandarkt.game.api.packets.outgoing

import com.arandarkt.game.api.packets.GamePacketEncoder
import com.arandarkt.game.api.packets.PacketHeader
import io.guthix.buffer.writeShortAdd
import io.netty.buffer.ByteBuf

class SmallVarbit(val id: Int, val value: Int) {
    companion object : GamePacketEncoder<SmallVarbit> {
        override val opcode: Int = 245
        override val header: PacketHeader = PacketHeader.NORMAL
        override fun encode(writer: ByteBuf, data: SmallVarbit) {
            writer.writeShortAdd(data.id)
            writer.writeByte(data.value)
        }
    }
}