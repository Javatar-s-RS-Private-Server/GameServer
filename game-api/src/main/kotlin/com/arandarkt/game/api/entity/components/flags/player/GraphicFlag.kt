package com.arandarkt.game.api.entity.components.flags.player

import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.entity.components.flags.FlagComponent
import com.arandarkt.game.api.entity.components.player.GraphicComponent
import io.guthix.buffer.writeIntME
import io.netty.buffer.ByteBuf

class GraphicFlag(val player: PlayerCharacter) : FlagComponent {
    override val flagId: Int = 0x200

    override fun ByteBuf.writeFlag() {
        val graphic = player.component<GraphicComponent>()

        writeShort(graphic.graphicId)
        writeIntME(graphic.height shl 16 or graphic.delay)

    }
}