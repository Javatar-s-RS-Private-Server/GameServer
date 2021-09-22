package com.arandarkt.game.api.packets

import com.arandarkt.game.api.packets.outgoing.*
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.koin.core.context.GlobalContext
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

fun <T> ByteBuf.transform(transformer: GamePacketDecoder<T>) : T  {
    return with(transformer) {
        decode()
    }
}

inline fun <reified T: Any> T.toPacket() : Packet {
    val scope = GlobalContext.get().createScope<T>()
    val packetBuffer = scope.get<ByteBuf> { parametersOf(this) }
    val encoder = scope.get<GamePacketEncoder<T>>()
    return Packet(
        encoder.header,
        encoder.opcode,
        packetBuffer
    )
}

inline fun <reified T : Any> Module.packet(encoder: GamePacketEncoder<T>) {
    scope<T> {
        scoped { encoder }
        factory { (packet: T) ->
            with(packet) {
                val buf = Unpooled.buffer()
                get<GamePacketEncoder<T>>().encode(buf, this)
                buf
            }
        }
    }
}

val packetModule = module {
    packet(OpenWindow)
    packet(OpenWidget)
    packet(CloseWidget)
    packet(BuildStaticRegion)
    packet(PlayerUpdates)
    packet(SmallVarbit)
    packet(LargeVarbit)
    packet(ClientScript)
    packet(GameMessage)
    packet(ClearMinimapFlag)
}