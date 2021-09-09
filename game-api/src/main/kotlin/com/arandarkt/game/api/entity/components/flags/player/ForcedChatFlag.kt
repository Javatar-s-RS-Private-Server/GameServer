package com.arandarkt.game.api.entity.components.flags.player

import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.entity.components.flags.FlagComponent
import com.arandarkt.game.api.entity.components.player.ChatComponent
import io.guthix.buffer.writeStringCP1252
import io.netty.buffer.ByteBuf

class ForcedChatFlag(val player: PlayerCharacter) : FlagComponent {
    override val flagId: Int = 0x80
    override fun ByteBuf.writeFlag() {
        val chat = player.component<ChatComponent>()

        writeStringCP1252(chat.forcedMessage)

    }
}