package com.arandarkt.game.widgets.tabs

import com.arandarkt.game.api.components.widgets.TabComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

class LogoutTab(override val player: PlayerCharacter) : TabComponent {
    override val tabIcon: Int = 109
    override val tabId: Int = 11
    override val widgetId: Int = 182
}