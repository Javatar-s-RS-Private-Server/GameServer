package com.arandarkt.game.api.packets.outgoing

import com.arandarkt.game.api.io.writeByteC
import com.arandarkt.game.api.packets.GamePacketEncoder
import com.arandarkt.game.api.packets.PacketHeader
import io.netty.buffer.ByteBuf

class OpenWidget(val windowId: Int, val windowChildId: Int, val widgetId: Int, val overlay: Boolean) {
    companion object : GamePacketEncoder<OpenWidget> {
        override val opcode: Int = 238
        override val header: PacketHeader = PacketHeader.NORMAL
        override fun encode(writer: ByteBuf, data: OpenWidget) {
            writer.writeInt(data.windowId shl 16 or data.windowChildId)
            writer.writeShort(data.widgetId)
            writer.writeByteC(if(data.overlay) 1 else 0)
        }
    }
}