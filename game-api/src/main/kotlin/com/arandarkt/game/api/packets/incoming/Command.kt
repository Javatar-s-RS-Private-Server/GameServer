package com.arandarkt.game.api.packets.incoming

import com.arandarkt.game.api.packets.GamePacketDecoder
import io.guthix.buffer.readStringCP1252
import io.netty.buffer.ByteBuf

class Command(val command: String) {
    companion object : GamePacketDecoder<Command> {
        override val opcode: Int = 89
        override fun ByteBuf.decode(): Command {
            skipBytes(1)
            return Command(readStringCP1252())
        }
    }
}