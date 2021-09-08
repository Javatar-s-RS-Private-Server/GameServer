package com.arandarkt.game.api.packets

fun interface PacketHandler<P> {

    fun handlePacket(packet: P)

}