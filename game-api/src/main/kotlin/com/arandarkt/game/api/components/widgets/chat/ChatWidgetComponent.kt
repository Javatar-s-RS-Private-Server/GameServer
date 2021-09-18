package com.arandarkt.game.api.components.widgets.chat

import com.arandarkt.game.api.components.widgets.WidgetComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

class ChatWidgetComponent(override val player: PlayerCharacter) : WidgetComponent {
    override val widgetId: Int = 137

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