package com.arandarkt.game.api.components.entity.flags.player

import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.components.entity.flags.FlagComponent
import com.arandarkt.game.api.components.entity.player.ChatComponent
import com.arandarkt.game.api.primitives.encryptPlayerChat
import io.guthix.buffer.writeByteAdd
import io.guthix.buffer.writeBytesReversed
import io.guthix.buffer.writeShortAddLE
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled

class ChatFlag(val player: PlayerCharacter) : FlagComponent {
    override val flagId: Int = 0x1

    override fun ByteBuf.writeFlag() {
        val chat = player.component<ChatComponent>()
        val chatStr = ByteArray(256)
        chatStr[0] = chat.message.length.toByte()
        val offset: Int =
            1 + encryptPlayerChat(chatStr, 0, 1, chat.message.length, chat.message.toByteArray())
        writeShortAddLE(chat.effects)
        writeByteAdd(chat.iconId)
        writeByte(offset + 1)
        writeBytesReversed(Unpooled.wrappedBuffer(chatStr))
    }
}