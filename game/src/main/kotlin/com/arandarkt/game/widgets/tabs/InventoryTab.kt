package com.arandarkt.game.widgets.tabs

import com.arandarkt.game.api.components.widgets.TabComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

class InventoryTab(override val player: PlayerCharacter) : TabComponent {
    override val tabId: Int = 3
    override val widgetId: Int = 149
    override val tabIcon: Int = 102
}