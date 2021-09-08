package com.arandarkt.game.api.packets.outgoing

import com.arandarkt.game.api.packets.GamePacketEncoder
import com.arandarkt.game.api.packets.PacketHeader
import io.netty.buffer.ByteBuf

class OpenWidgetPacket(val windowId: Int, val componentId: Int, val widgetId: Int, val overlay: Boolean) {
    companion object : GamePacketEncoder<OpenWidgetPacket> {
        override val opcode: Int = 238
        override val header: PacketHeader = PacketHeader.NORMAL
        override fun encode(writer: ByteBuf, data: OpenWidgetPacket) {
            writer.writeInt(data.componentId shl 16 and data.windowId)
            writer.writeShort(data.widgetId)
            writer.writeBoolean(data.overlay)
        }
    }
}