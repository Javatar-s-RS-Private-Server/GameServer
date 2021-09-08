package com.arandarkt.game.api.packets

import io.netty.buffer.ByteBuf

interface GamePacketDecoder<P> {

    val opcode: Int
    fun ByteBuf.decode() : P

}