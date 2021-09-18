package com.arandarkt.game.api.packets.outgoing

import com.arandarkt.game.api.packets.GamePacketEncoder
import com.arandarkt.game.api.packets.PacketHeader
import io.netty.buffer.ByteBuf

class OpenWidget(val windowId: Int, val windowChildId: Int, val widgetId: Int, val overlay: Boolean) {
    companion object : GamePacketEncoder<OpenWidget> {
        override val opcode: Int = 111
        override val header: PacketHeader = PacketHeader.NORMAL
        override fun encode(writer: ByteBuf, data: OpenWidget) {
            writer.writeByte(if(data.overlay) 1 else 0)
            writer.writeShort(data.widgetId)
            writer.writeInt(data.windowId shl 16 or data.windowChildId)
        }
    }
}