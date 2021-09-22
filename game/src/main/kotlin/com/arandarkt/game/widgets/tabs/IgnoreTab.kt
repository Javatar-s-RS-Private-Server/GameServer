package com.arandarkt.game.widgets.tabs

import com.arandarkt.game.api.components.widgets.TabComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

class IgnoreTab(override val player: PlayerCharacter) : TabComponent {
    override val tabIcon: Int = 108
    override val tabId: Int = 9
    override val widgetId: Int = 551
}