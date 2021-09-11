package com.arandarkt.game.api.entity.widget

import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.packets.PacketHandler
import com.arandarkt.game.api.packets.incoming.widgets.WidgetAction

class LegacyWidgetActionHandler(val player: PlayerCharacter) : PacketHandler<WidgetAction> {
    override fun handlePacket(packet: WidgetAction) {
        val manager = player.widgetManager
        if(manager.widget?.widgetId == packet.parentId) {
            manager.widget?.onButtonClicked(packet.childId)
        } else if(manager.tabs.isTabSelected(packet.parentId)) {
            manager.tabs.onButtonClicked(packet.childId, packet.option)
        }
    }
}