package com.arandarkt.game.api.components.entity.flags.player

import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.components.entity.flags.FlagComponent
import com.arandarkt.game.api.components.entity.player.ForcedMovementComponent
import com.arandarkt.game.api.components.entity.player.UpdateMasksComponent
import com.arandarkt.game.api.io.writeByteC
import io.guthix.buffer.writeByteAdd
import io.guthix.buffer.writeByteSub
import io.guthix.buffer.writeShortAdd
import io.netty.buffer.ByteBuf

class ForcedMovementFlag(val player: PlayerCharacter) : FlagComponent {
    override val flagId: Int = 0x400

    override fun ByteBuf.writeFlag() {
        val masks = player.component<UpdateMasksComponent>()
        val mov = player.component<ForcedMovementComponent>()
        writeByteSub(mov.forcedStartPosition.getSceneX(masks.lastSceneGraph))
        writeByteSub(mov.forcedStartPosition.getSceneY(masks.lastSceneGraph))
        writeByteSub(mov.forcedEndPosition.getSceneX(masks.lastSceneGraph))
        writeByteAdd(mov.forcedEndPosition.getSceneY(masks.lastSceneGraph))
        writeShortAdd(mov.commenceSpeed * 30)
        writeShort((mov.commenceSpeed * 30) + (mov.pathSpeed * 30 + 1))
        writeByteC(mov.direction.toInteger())
    }
}