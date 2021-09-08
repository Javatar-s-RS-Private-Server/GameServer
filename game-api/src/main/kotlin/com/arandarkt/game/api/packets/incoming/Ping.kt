package com.arandarkt.game.api.packets.incoming

import com.arandarkt.game.api.packets.GamePacketDecoder
import io.netty.buffer.ByteBuf

class Ping {
    companion object : GamePacketDecoder<Ping> {
        override val opcode: Int = 202
        override fun ByteBuf.decode(): Ping {
            return Ping()
        }
    }
}