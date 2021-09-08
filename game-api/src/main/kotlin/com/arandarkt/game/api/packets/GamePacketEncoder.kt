package com.arandarkt.game.api.packets

import io.netty.buffer.ByteBuf

interface GamePacketEncoder<T> {

    val opcode: Int
    val header: PacketHeader
    fun encode(writer: ByteBuf, data: T)

}