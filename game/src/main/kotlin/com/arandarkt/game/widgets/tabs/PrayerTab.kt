package com.arandarkt.game.widgets.tabs

import com.arandarkt.game.api.components.widgets.TabComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

class PrayerTab(override val player: PlayerCharacter) : TabComponent {
    override val tabId: Int = 5
    override val tabIcon: Int = 104
    override val widgetId: Int = 271
}