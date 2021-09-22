package com.arandarkt.game.widgets.tabs

import com.arandarkt.game.api.components.widgets.TabComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

class MagicTab(override val player: PlayerCharacter) : TabComponent {
    override val tabIcon: Int = 105
    override val tabId: Int = 6
    override val widgetId: Int = 192
}