package com.arandarkt.game.api.packets.incoming.walking.decoders

import com.arandarkt.game.api.packets.GamePacketDecoder
import com.arandarkt.game.api.packets.incoming.walking.Movement
import io.netty.buffer.ByteBuf

object ViewportMovement : GamePacketDecoder<Movement> {
    override val opcode: Int = 184
    override fun ByteBuf.decode(): Movement {
        val size = readUnsignedByte()
        val isRunning = readBoolean()
        var x = readUnsignedShort()
        var y = readUnsignedShort()

        val steps = (size - 5) / 2

        for (i in 0 until steps) {
            val offsetX: Int = readUnsignedByte().toInt()
            val offsetY: Int = readUnsignedByte().toInt()
            if (i == steps - 1) {
                x += offsetX
                y += offsetY
            }
        }

        return Movement(isRunning, x, y)
    }
}