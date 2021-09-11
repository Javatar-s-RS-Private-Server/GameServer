package com.arandarkt.game.api.packets.outgoing

import com.arandarkt.game.api.packets.GamePacketEncoder
import com.arandarkt.game.api.packets.PacketHeader
import io.netty.buffer.ByteBuf

class CloseWidget(val windowId: Int, val widgetId: Int) {
    companion object : GamePacketEncoder<CloseWidget> {
        override val opcode: Int = 137
        override val header: PacketHeader = PacketHeader.NORMAL
        override fun encode(writer: ByteBuf, data: CloseWidget) {
            writer.writeShort(data.windowId)
            writer.writeShort(data.widgetId)
        }
    }
}