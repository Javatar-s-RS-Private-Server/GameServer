package com.arandarkt.game.widgets.tabs

import com.arandarkt.game.api.components.widgets.TabComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

class EquipmentTab(override val player: PlayerCharacter) : TabComponent {
    override val tabId: Int = 4
    override val tabIcon: Int = 103
    override val widgetId: Int = 387
}