package com.arandarkt.network.channel.codec.packets

import com.arandarkt.game.api.packets.Packet
import com.arandarkt.network.channel.NetworkSession.Companion.gameSession
import com.arandarkt.game.api.packets.PacketHeader
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

class PacketDecoder : ByteToMessageDecoder() {
    override fun decode(ctx: ChannelHandlerContext, buffer: ByteBuf, out: MutableList<Any>) {
        val session = ctx.channel().gameSession
        val opcode = buffer.readUnsignedByte().toInt()

        if(opcode >= PACKET_SIZES.size) {
            return
        }

        val header = PACKET_SIZES[opcode]
        val size = if(header < 0) {
            buffer.readHeader(header)
        } else header

        if(size != -1 && size <= buffer.writerIndex()) {
            val packetBuffer = buffer.readBytes(size)
            val packetHeader = when (header) {
                -1 -> PacketHeader.BYTE
                -2 -> PacketHeader.SHORT
                else -> PacketHeader.NORMAL
            }
            if (opcode != 202 && opcode != 99) {
                println("Incoming Packet $opcode - $size - $header")
            }
            session.receivePacket(Packet(packetHeader, opcode, packetBuffer))
        }

    }

    private fun ByteBuf.readHeader(header: Int): Int {
        return when(header) {
            -1 -> readUnsignedByte().toInt()
            -2 -> readUnsignedShort()
            -3 -> readableBytes()
            else -> -1
        }
    }

    companion object {
        val PACKET_SIZES = intArrayOf(
            -3, -3, 8, -3, -3, -3, -3, 1, -3, -3,  // 0
            -3, -3, -3, -3, -3, -3, -3, -3, -3,  /*-3*/2,  // 10
            -3, -3, -3, -3, -3, -3, -3, -3,  /*-3*/6, -3,  // 20
            -3, -3, -3, -3, -3, -3, -1, 6, -3, -3,  // 30
            -3, -3, -3, -1, 6, -3, -3, -3, -3, 13,  // 40
            -1, 2, -3, -3, -3, -3, -3, -3, -3, -3,  // 50
            -3, -3, -3, 12, -3, -3, -3, -3, 3, 8,  // 60
            -3, 0, -3, -3, -3, -3, -3, -3, 4, -3,  // 70
            -3, -3, -3, -3, 2, -3, 8, -3, 8, -3,  // 80
            -3, -3, -3, -3, -3, -3, -3, -3, -3, 4,  // 90
            -3, 8, -3, -3, -3, 14, -3, -3, -3, -3,  // 100
            -3, -3, -3, 6, -3, -1, -3, -3, -3, 6,  // 110
            6, 9, 8, 8, -3, -3, -3, -3, -3, 2,  // 120
            9, -3, -3, -3, 6, -3, -3, 6, -3, -3,  // 130
            6, -3, -3, -1, -3, -3, -3, -3, -3, -3,  // 140
            -3, -3, -3, 4, 8, -3, 2, -3, -3, 8,  // 150
            -3, 8, -3, 14, -3, -1, 16, -3, -3, -3,  // 160
            -3, -3, -3, -3, 4, -3, 2, 8, -3, -3,  // 170
            -3, -3, -3, 2, -3, 1, -3, 10, -3, -3,  // 180
            -3, -3, -3, -3, -3, -3, -3, -3, -3, -3,  // 190
            -3, -3, 0, -3, -3, 2, -3, -3, -3, -3,  // 200
            -3, -3, 8, -3, -3, 8, 6, -3, -3, -3,  // 210
            8, -3, -3, -3, 8, -3, -3, -3, -3, -3,  // 220
            0, -3, -3, -3, -3, -3, -3, -3, -1, -3,  // 230
            4, -3, -3, -3, -3, -3, -3, 8, -3, -3,  // 240
            -3, -3, -3, -3, -3, -3 // 250
        )
    }
}