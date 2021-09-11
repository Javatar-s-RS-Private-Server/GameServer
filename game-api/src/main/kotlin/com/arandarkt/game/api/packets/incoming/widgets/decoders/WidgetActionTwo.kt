package com.arandarkt.game.api.packets.incoming.widgets.decoders

import com.arandarkt.game.api.packets.GamePacketDecoder
import com.arandarkt.game.api.packets.incoming.widgets.WidgetAction
import io.netty.buffer.ByteBuf

object WidgetActionTwo : GamePacketDecoder<WidgetAction> {
    override val opcode: Int = 113
    override fun ByteBuf.decode(): WidgetAction {
        return WidgetAction(readUnsignedShort(), readUnsignedShort(), 2)
    }
}