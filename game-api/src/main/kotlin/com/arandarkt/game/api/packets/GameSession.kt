package com.arandarkt.game.api.packets

interface GameSession {
    fun sendPacket(packet: Packet)
    fun <T> handlePacket(transformer: GamePacketDecoder<T>, action: suspend (T) -> Unit)
    fun <T> handlePacket(transformer: GamePacketDecoder<T>, action: PacketHandler<T>)
    fun isConnected(): Boolean
    fun disconnect()
    fun flush()

    companion object {

        inline fun <reified T : Any> GameSession.sendPacket(value: T) {
            sendPacket(value.toPacket())
        }

    }

}