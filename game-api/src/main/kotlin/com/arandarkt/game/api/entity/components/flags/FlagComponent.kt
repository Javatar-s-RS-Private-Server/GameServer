package com.arandarkt.game.api.entity.components.flags

import io.netty.buffer.ByteBuf

interface FlagComponent {

    val flagId: Int
    fun ByteBuf.writeFlag()

}