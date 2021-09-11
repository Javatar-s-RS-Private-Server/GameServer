package com.arandarkt.game.api.components.entity.flags.player

import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.components.entity.flags.FlagComponent
import com.arandarkt.game.api.components.entity.player.FaceEntityOrPositionComponent
import io.guthix.buffer.writeShortAddLE
import io.netty.buffer.ByteBuf

class FacePositionFlag(val player: PlayerCharacter) : FlagComponent {
    override val flagId: Int = 0x4
    override fun ByteBuf.writeFlag() {
        val faceComp = player.component<FaceEntityOrPositionComponent>()
        val pos = faceComp.position
        writeShortAddLE((pos.x shl 1) + 1)
        writeShortAddLE((pos.y shl 1) + 1)
    }
}