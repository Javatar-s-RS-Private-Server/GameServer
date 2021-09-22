package com.arandarkt.game.widgets.tabs

import com.arandarkt.game.api.components.widgets.TabComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

class QuestTab(override val player: PlayerCharacter) : TabComponent {
    override val tabIcon: Int = 101
    override val widgetId: Int = 274
    override val tabId: Int = 2

    override fun onButtonClicked(buttonId: Int, option: Int) {
    }

    override fun onOpen() {
    }

    override fun onClose() {
    }
}