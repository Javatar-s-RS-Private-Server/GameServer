package com.arandarkt.game.api.components.entity.flags

import io.netty.buffer.ByteBuf

interface FlagComponent {

    val flagId: Int
    fun ByteBuf.writeFlag()

}