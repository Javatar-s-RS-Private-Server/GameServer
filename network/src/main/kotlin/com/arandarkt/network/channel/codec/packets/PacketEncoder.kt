package com.arandarkt.network.channel.codec.packets

import com.arandarkt.game.api.packets.Packet
import com.arandarkt.network.channel.NetworkSession.Companion.gameSession
import com.arandarkt.game.api.packets.PacketHeader
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class PacketEncoder : MessageToByteEncoder<Packet>() {
    override fun encode(ctx: ChannelHandlerContext, msg: Packet, out: ByteBuf) {
        val session = ctx.channel().gameSession
        val outCipher = session.outCipher

        if(msg.opcode != -1) {

            println("Encoding packet ${msg.opcode}")

            out.writeByte(msg.opcode) //Out Cipher here
            when(msg.header) {
                PacketHeader.NORMAL -> {}
                PacketHeader.BYTE -> out.writeByte(msg.packetBuffer.writerIndex())
                PacketHeader.SHORT -> out.writeShort(msg.packetBuffer.writerIndex())
            }

            out.writeBytes(msg.packetBuffer)

        }

    }
}