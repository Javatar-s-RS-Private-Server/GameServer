package com.arandarkt.game.api.components.entity.flags.player

import com.arandarkt.game.api.components.entity.flags.FlagComponent
import com.arandarkt.game.api.entity.character.Character
import io.netty.buffer.ByteBuf

class FaceEntityFlag(val index: Int) : FlagComponent {
    override val flagId: Int = 0x8

    override fun ByteBuf.writeFlag() {
        writeShort(index)
    }
}