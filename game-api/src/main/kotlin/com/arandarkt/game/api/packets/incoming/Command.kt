package com.arandarkt.game.api.packets.incoming

import com.arandarkt.game.api.packets.GamePacketDecoder
import io.guthix.buffer.readStringCP1252
import io.netty.buffer.ByteBuf

class Command(val command: String) {
    companion object : GamePacketDecoder<Command> {
        override val opcode: Int = 165
        override fun ByteBuf.decode(): Command {
            return Command(readStringCP1252())
        }
    }
}