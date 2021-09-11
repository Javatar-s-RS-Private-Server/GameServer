package com.arandarkt.game.api.packets.outgoing

import com.arandarkt.game.api.packets.GamePacketEncoder
import com.arandarkt.game.api.packets.PacketHeader
import io.guthix.buffer.writeStringCP1252
import io.netty.buffer.ByteBuf

class ClientScript(val scriptId: Int, val sig: String, val args: List<Any> = emptyList()) {
    companion object : GamePacketEncoder<ClientScript> {
        override val opcode: Int = 69
        override val header: PacketHeader = PacketHeader.SHORT
        override fun encode(writer: ByteBuf, data: ClientScript) {
            writer.writeStringCP1252(data.sig)
            val string = data.sig
            for ((j, i) in (string.length - 1 downTo 0).withIndex()) {
                if (string[i] == 's') {
                    writer.writeStringCP1252(data.args[j] as String)
                } else {
                    writer.writeInt(data.args[j] as Int)
                }
            }
            writer.writeInt(data.scriptId)
        }
    }
}