package com.arandarkt.network.channel.session.packet

import com.arandarkt.game.api.packets.GamePacketDecoder
import com.arandarkt.game.api.packets.PacketHandler
import com.arandarkt.network.channel.NettyChannel
import com.arandarkt.game.api.packets.Packet
import com.arandarkt.game.api.world.Ticker.Companion.GAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.CancellationException

class PacketChannel(private val channel: NettyChannel) {

    val incoming = MutableSharedFlow<Packet>(extraBufferCapacity = 100)
    val outgoing = Channel<Packet>(40, BufferOverflow.DROP_LATEST)

    val subscribedPackets = mutableListOf<Job>()

    fun receivePacket(packet: Packet) {
        incoming.tryEmit(packet)
    }

    fun sendPacket(packet: Packet) {
        outgoing.trySend(packet)
    }

    fun flushPackets() {
        repeat(40) {
            val packet = outgoing.tryReceive()
            if(packet.isSuccess) {
                val p = packet.getOrNull()
                if(p != null) {
                    channel.write(p)
                }
            } else if(packet.isClosed) {
                return@repeat
            }
        }
        channel.flush()
    }

    fun <T> handlePacket(transformer: GamePacketDecoder<T>, action: suspend (T) -> Unit) {
        subscribedPackets.add(packet(transformer, action))
    }

    fun <T> handlePacket(transformer: GamePacketDecoder<T>, action: PacketHandler<T>) {
        subscribedPackets.add(packet(transformer) { action.handlePacket(it) })
    }

    fun <T> packet(transformer: GamePacketDecoder<T>, action: suspend (T) -> Unit) = incoming
        .filter { it.opcode == transformer.opcode }
        .transform {
            with(transformer) {
                emit(it.packetBuffer.decode())
            }
        }
        .flowOn(Dispatchers.IO)
        .onEach { action(it) }
        .launchIn(GAME)

    fun shutdown() {
        outgoing.close()
        subscribedPackets.forEach { it.cancel(CancellationException("Shutting Down")) }
    }

}