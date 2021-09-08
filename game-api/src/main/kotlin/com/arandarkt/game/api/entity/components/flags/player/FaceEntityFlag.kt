package com.arandarkt.game.api.entity.components.flags.player

import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.entity.components.flags.FlagComponent
import com.arandarkt.game.api.entity.components.player.FaceEntityOrPositionComponent
import io.netty.buffer.ByteBuf

class FaceEntityFlag(val player: PlayerCharacter) : FlagComponent {
    override val flagId: Int = 0x8

    override fun ByteBuf.writeFlag() {
        val faceComp = player.component<FaceEntityOrPositionComponent>()

        writeShortLE(faceComp.entityIndex)

    }
}