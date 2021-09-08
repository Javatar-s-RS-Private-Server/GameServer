package com.arandarkt.network.channel

import com.arandarkt.network.channel.session.packet.PacketSession
import com.arandarkt.network.channel.session.HandshakeSession
import com.arandarkt.network.channel.session.LoginSession
import io.netty.channel.Channel
import io.netty.util.AttributeKey

interface NetworkSession {

    companion object {
        val SESSION_KEY = AttributeKey.valueOf<NetworkSession>("session")

        val Channel.handshakeSession: HandshakeSession
            get() = attr(SESSION_KEY).get() as HandshakeSession
        val Channel.loginSession: LoginSession
            get() = attr(SESSION_KEY).get() as LoginSession
        val Channel.gameSession: PacketSession
            get() = attr(SESSION_KEY).get() as PacketSession

        val Channel.session: NetworkSession get() = attr(SESSION_KEY).get()
    }
}