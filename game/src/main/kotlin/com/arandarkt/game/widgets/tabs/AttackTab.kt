package com.arandarkt.game.widgets.tabs

import com.arandarkt.game.api.components.widgets.TabComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

class AttackTab(override val player: PlayerCharacter) : TabComponent {
    override val widgetId: Int = 92
    override val tabIcon: Int = 99
    override val tabId: Int = 0

    override fun onButtonClicked(buttonId: Int, option: Int) {

    }

    override fun onOpen() {
    }

    override fun onClose() {
    }
}