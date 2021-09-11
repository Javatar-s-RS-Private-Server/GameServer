package com.arandarkt.game.api.components.entity.flags.player

import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.components.entity.flags.FlagComponent
import com.arandarkt.game.api.components.entity.player.AnimationComponent
import com.arandarkt.game.api.io.writeByteC
import io.netty.buffer.ByteBuf

class AnimationFlag(val player: PlayerCharacter) : FlagComponent {
    override val flagId: Int = 0x20
    override fun ByteBuf.writeFlag() {
        val anim = player.component<AnimationComponent>()
        writeShortLE(anim.animationId)
        writeByteC(anim.animationDelay)
    }
}