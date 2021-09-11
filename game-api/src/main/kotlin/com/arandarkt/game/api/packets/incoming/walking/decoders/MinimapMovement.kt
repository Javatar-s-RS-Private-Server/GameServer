package com.arandarkt.game.api.packets.incoming.walking.decoders

import com.arandarkt.game.api.packets.GamePacketDecoder
import com.arandarkt.game.api.packets.incoming.walking.Movement
import io.guthix.buffer.readByteSub
import io.guthix.buffer.readUnsignedByteSub
import io.guthix.buffer.readUnsignedShortAdd
import io.netty.buffer.ByteBuf

object MinimapMovement : GamePacketDecoder<Movement> {
    override val opcode: Int = 143
    override fun ByteBuf.decode(): Movement {
        val isRunning = readByteSub().toInt() == 1
        var y = readUnsignedShortLE()
        var x = readUnsignedShortAdd()

        val size = ((capacity() - readerIndex()) - 14)
        val steps = (size - 5) / 2

        for (i in 0 until steps) {
            val offsetX: Int = readUnsignedByteSub().toInt()
            val offsetY: Int = readUnsignedByteSub().toInt()
            if (i == steps - 1) {
                x += offsetX
                y += offsetY
            }
        }

        return Movement(isRunning, x, y)
    }
}