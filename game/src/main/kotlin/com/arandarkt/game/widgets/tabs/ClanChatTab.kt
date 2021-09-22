package com.arandarkt.game.widgets.tabs

import com.arandarkt.game.api.components.widgets.TabComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

class ClanChatTab(override val player: PlayerCharacter) : TabComponent {
    override val tabIcon: Int = 106
    override val tabId: Int = 7
    override val widgetId: Int = 589
}