package com.arandarkt.network.channel.codec.packets

import com.arandarkt.game.api.packets.Packet
import com.arandarkt.network.channel.NetworkSession.Companion.gameSession
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class ArandarPacketHandler : SimpleChannelInboundHandler<Packet>() {

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Packet) {

    }

    override fun channelUnregistered(ctx: ChannelHandlerContext) {
        val session = ctx.channel().gameSession
        session.disconnect()
    }
}