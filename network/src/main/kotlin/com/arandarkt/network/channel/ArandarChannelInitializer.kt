package com.arandarkt.network.channel

import com.arandarkt.network.channel.NetworkSession.Companion.SESSION_KEY
import com.arandarkt.network.channel.codec.handshake.ArandarHandshakeHandler
import com.arandarkt.network.channel.codec.handshake.HandshakeDecoder
import com.arandarkt.network.channel.codec.handshake.HandshakeEncoder
import com.arandarkt.network.channel.session.HandshakeSession
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel

class ArandarChannelInitializer : ChannelInitializer<SocketChannel>() {
    override fun initChannel(ch: SocketChannel) {

        ch.attr(SESSION_KEY).setIfAbsent(HandshakeSession())

        ch.pipeline().addLast("decoder", HandshakeDecoder())
        ch.pipeline().addLast("encoder", HandshakeEncoder())
        ch.pipeline().addLast("handler", ArandarHandshakeHandler())

    }
}