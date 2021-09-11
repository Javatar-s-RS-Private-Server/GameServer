package com.arandarkt.game.api.packets.outgoing

import com.arandarkt.game.api.packets.GamePacketEncoder
import com.arandarkt.game.api.packets.PacketHeader
import io.guthix.buffer.writeShortAdd
import io.netty.buffer.ByteBuf

class LargeVarbit(val id: Int, val value: Int) {
    companion object : GamePacketEncoder<LargeVarbit> {
        override val opcode: Int = 37
        override val header: PacketHeader = PacketHeader.NORMAL
        override fun encode(writer: ByteBuf, data: LargeVarbit) {
            writer.writeShortAdd(data.id)
            writer.writeIntLE(data.value)
        }
    }
}