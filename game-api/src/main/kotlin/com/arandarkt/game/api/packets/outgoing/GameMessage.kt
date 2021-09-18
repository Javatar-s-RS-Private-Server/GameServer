package com.arandarkt.game.api.packets.outgoing

import com.arandarkt.game.api.packets.GamePacketEncoder
import com.arandarkt.game.api.packets.PacketHeader
import io.guthix.buffer.writeStringCP1252
import io.netty.buffer.ByteBuf

class GameMessage(val message: String) {
    companion object : GamePacketEncoder<GameMessage> {
        override val opcode: Int = 238
        override val header: PacketHeader = PacketHeader.BYTE
        override fun encode(writer: ByteBuf, data: GameMessage) {
            writer.writeStringCP1252(data.message)
        }
    }
}