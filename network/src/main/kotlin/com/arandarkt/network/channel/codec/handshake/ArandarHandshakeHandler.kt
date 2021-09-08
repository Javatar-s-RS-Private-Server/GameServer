package com.arandarkt.network.channel.codec.handshake

import com.arandarkt.network.channel.NetworkSession.Companion.SESSION_KEY
import com.arandarkt.network.channel.NetworkSession.Companion.handshakeSession
import com.arandarkt.network.channel.codec.login.ArandarLoginHandler
import com.arandarkt.network.channel.codec.login.LoginDecoder
import com.arandarkt.network.channel.codec.login.LoginEncoder
import com.arandarkt.network.channel.session.LoginSession
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class ArandarHandshakeHandler : SimpleChannelInboundHandler<HandshakeResponse>() {
    override fun channelRead0(ctx: ChannelHandlerContext, msg: HandshakeResponse) {
        ctx.writeAndFlush(msg)
        if(msg === HandshakeResponse.LOGIN) {
            val session = ctx.channel().handshakeSession
            ctx.channel().attr(SESSION_KEY).set(LoginSession(session.sessionKey))

            ctx.pipeline().replace("decoder", "decoder", LoginDecoder())
            ctx.pipeline().replace("encoder", "encoder", LoginEncoder())
            ctx.pipeline().replace("handler", "handler", ArandarLoginHandler())
        }
    }
}