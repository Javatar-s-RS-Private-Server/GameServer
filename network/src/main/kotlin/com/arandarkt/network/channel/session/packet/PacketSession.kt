package com.arandarkt.network.channel.session.packet

import com.arandarkt.game.api.packets.*
import com.arandarkt.network.channel.NetworkSession
import com.arandarkt.network.cipher.IsaacCipher
import io.netty.channel.Channel

class PacketSession(
    val sessionKey: Long,
    val inCipher: IsaacCipher,
    val outCipher: IsaacCipher,
    val channel: Channel
) : NetworkSession, GameSession {

    private val packetsChannel = PacketChannel(channel)

    fun receivePacket(packet: Packet) {
        packetsChannel.receivePacket(packet)
    }

    override fun sendPacket(packet: Packet) {
        packetsChannel.sendPacket(packet)
    }

    override fun <T> handlePacket(transformer: GamePacketDecoder<T>, action: suspend (T) -> Unit) {
        packetsChannel.handlePacket(transformer, action)
    }

    override fun <T> handlePacket(transformer: GamePacketDecoder<T>, action: PacketHandler<T>) {
        packetsChannel.handlePacket(transformer, action)
    }

    override fun isConnected(): Boolean {
        return channel.isActive
    }

    override fun disconnect() {
        packetsChannel.shutdown()
        if (channel.isActive) {
            channel.disconnect()
        }
    }

    override fun flush() {
        packetsChannel.flushPackets()
    }

}