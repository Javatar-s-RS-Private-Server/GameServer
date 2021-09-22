package com.arandarkt.game.api.components.entity.flags.player

import com.arandarkt.game.api.components.entity.flags.FlagComponent
import com.arandarkt.game.api.world.location.components.Position
import io.guthix.buffer.writeShortAddLE
import io.netty.buffer.ByteBuf

class FacePositionFlag(val pos : Position) : FlagComponent {
    override val flagId: Int = 0x1
    override fun ByteBuf.writeFlag() {
        writeShort((pos.x * 2) + 1)
        writeShort((pos.y * 2) + 1)
    }
}