package com.arandarkt.game.widgets.tabs

import com.arandarkt.game.api.components.widgets.TabComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

class MusicTab(override val player: PlayerCharacter) : TabComponent {
    override val tabIcon: Int = 112
    override val tabId: Int = 13
    override val widgetId: Int = 239
}