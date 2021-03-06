package com.arandarkt.game.api.packets.outgoing

import com.arandarkt.game.api.packets.GamePacketEncoder
import com.arandarkt.game.api.packets.PacketHeader
import io.guthix.buffer.writeShortAdd
import io.guthix.buffer.writeShortAddLE
import io.netty.buffer.ByteBuf

class OpenWindow(val windowId: Int) {
    companion object : GamePacketEncoder<OpenWindow> {
        override val opcode: Int = 27
        override val header: PacketHeader = PacketHeader.NORMAL
        override fun encode(writer: ByteBuf, data: OpenWindow) {
            writer.writeShortAdd(data.windowId)
        }
    }
}