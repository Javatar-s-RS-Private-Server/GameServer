package com.arandarkt.network.channel.codec.handshake

import com.arandarkt.network.channel.NetworkSession.Companion.handshakeSession
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class HandshakeEncoder : MessageToByteEncoder<HandshakeResponse>() {
    override fun encode(ctx: ChannelHandlerContext, msg: HandshakeResponse, out: ByteBuf) {
        out.writeByte(0)
        if(msg === HandshakeResponse.LOGIN) {
            val key = ctx.channel().handshakeSession.sessionKey
            println("Session key $key")
            out.writeLong(key)
        }
    }
}