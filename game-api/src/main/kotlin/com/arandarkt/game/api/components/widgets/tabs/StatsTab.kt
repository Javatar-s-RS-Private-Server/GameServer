package com.arandarkt.game.api.components.widgets.tabs

import com.arandarkt.game.api.components.widgets.TabComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.packets.GameSession.Companion.sendPacket
import com.arandarkt.game.api.packets.outgoing.GameMessage

class StatsTab(override val player: PlayerCharacter) : TabComponent {
    override val tabId: Int = 1
    override val widgetId: Int = 320

    override fun onButtonClicked(buttonId: Int, option: Int) {
        when(option) {
            1 -> handleOptionOne(buttonId)
            2 -> handleOptionTwo(buttonId)
            3 -> handleOptionThree(buttonId)
        }
    }

    private fun handleOptionOne(buttonId: Int) {

    }

    private fun handleOptionTwo(buttonId: Int) {

    }

    private fun handleOptionThree(buttonId: Int) {

    }

    override fun onOpen() {

    }

    override fun onClose() {

    }
}