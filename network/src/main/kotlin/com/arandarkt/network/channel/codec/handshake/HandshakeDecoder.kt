package com.arandarkt.network.channel.codec.handshake

import com.arandarkt.network.channel.NetworkSession.Companion.handshakeSession
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import java.util.concurrent.ThreadLocalRandom

class HandshakeDecoder : ByteToMessageDecoder() {
    override fun decode(ctx: ChannelHandlerContext, buffer: ByteBuf, out: MutableList<Any>) {
        val handshakeOpcode = buffer.readUnsignedByte().toInt()
        if(handshakeOpcode == 14) {
            val nameHash = buffer.readByte().toInt()
            ctx.channel().handshakeSession.nameHash = nameHash
            ctx.channel().handshakeSession.sessionKey = ThreadLocalRandom.current().nextLong()
            out.add(HandshakeResponse.LOGIN)
        } else {
            out.add(HandshakeResponse.JS5)
        }
    }
}