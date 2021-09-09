package com.arandarkt.game.api.io

import io.guthix.buffer.BitBuf

fun BitBuf.writeInt(value: Int) {
    writeBits(value, 32)
}

fun BitBuf.writeDouble(value: Double) {
    toByteMode().writeDouble(value)
}

fun BitBuf.writeByte(value: Int) {
    writeBits(value, 8)
}

fun BitBuf.writeShort(value: Int) {
    writeBits(value, 16)
}

fun BitBuf.readInt() = readBits(32)
fun BitBuf.readUnsignedByte() = readBits(8) and 255
fun BitBuf.readUnsignedShort() = readBits(16) and 65535
fun BitBuf.readDouble() = toByteMode().readDouble()

fun BitBuf.array() = toByteMode().array()