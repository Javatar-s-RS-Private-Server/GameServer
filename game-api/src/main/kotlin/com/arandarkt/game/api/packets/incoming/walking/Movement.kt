package com.arandarkt.game.api.packets.incoming.walking

import com.arandarkt.game.api.packets.GamePacketDecoder
import io.guthix.buffer.readByteSub
import io.guthix.buffer.readUnsignedByteSub
import io.guthix.buffer.readUnsignedShortAdd
import io.netty.buffer.ByteBuf

class Movement(val isRunning: Boolean, val x: Int, val y: Int) {

}