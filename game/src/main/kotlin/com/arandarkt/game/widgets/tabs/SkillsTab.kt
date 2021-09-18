package com.arandarkt.game.widgets.tabs

import com.arandarkt.game.api.components.widgets.TabComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

class SkillsTab(override val player: PlayerCharacter) : TabComponent {
    override val tabIcon: Int = 100
    override val widgetId: Int = 320

    override fun onButtonClicked(buttonId: Int, option: Int) {
        TODO("Not yet implemented")
    }

    override fun onOpen() {
        TODO("Not yet implemented")
    }

    override fun onClose() {
        TODO("Not yet implemented")
    }
}