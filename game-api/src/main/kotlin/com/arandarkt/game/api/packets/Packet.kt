package com.arandarkt.game.api.packets

import io.netty.buffer.ByteBuf

class Packet(val header: PacketHeader, val opcode: Int, val packetBuffer: ByteBuf)