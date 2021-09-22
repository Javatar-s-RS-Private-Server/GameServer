package com.arandarkt.game.widgets.tabs

import com.arandarkt.game.api.components.widgets.TabComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

class EmoteTab(override val player: PlayerCharacter) : TabComponent {
    override val tabIcon: Int = 111
    override val tabId: Int = 12
    override val widgetId: Int = 464
}