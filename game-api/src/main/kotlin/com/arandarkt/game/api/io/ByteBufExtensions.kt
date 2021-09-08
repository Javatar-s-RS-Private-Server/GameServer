package com.arandarkt.game.api.io

import io.netty.buffer.ByteBuf

fun ByteBuf.writeByteC(value: Int) {
    writeByte(-value)
}