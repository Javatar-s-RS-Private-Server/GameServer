package com.arandarkt.game.api.packets

import com.arandarkt.game.api.components.Component

interface GameSession : Component {
    fun sendPacket(packet: Packet)
    fun <T> handlePacket(transformer: GamePacketDecoder<T>, action: suspend (T) -> Unit)
    fun <T> handlePacket(transformer: GamePacketDecoder<T>, action: PacketHandler<T>)
    fun isConnected(): Boolean
    fun disconnect()
    fun flush()

    override suspend fun onTick(currentTick: Long): Boolean {
        if (isConnected()) {
            flush()
            return false
        }
        return true
    }


    companion object {
        inline fun <reified T : Any> GameSession.sendPacket(value: T) {
            sendPacket(value.toPacket())
        }
    }
}