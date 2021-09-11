package com.arandarkt.game.api.packets.incoming.widgets.decoders

import com.arandarkt.game.api.packets.GamePacketDecoder
import com.arandarkt.game.api.packets.incoming.widgets.WidgetAction
import io.netty.buffer.ByteBuf

object WidgetActionOne : GamePacketDecoder<WidgetAction> {
    override val opcode: Int = 153
    override fun ByteBuf.decode(): WidgetAction {
        return WidgetAction(readUnsignedShort(), readUnsignedShort(), 1)
    }
}