package com.arandarkt.game.widgets.tabs

import com.arandarkt.game.api.components.widgets.TabComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

class FriendsTab(override val player: PlayerCharacter) : TabComponent {
    override val tabIcon: Int = 107
    override val tabId: Int = 8
    override val widgetId: Int = 550
}