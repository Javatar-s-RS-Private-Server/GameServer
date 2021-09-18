package com.arandarkt.game.api.components.widgets.frame

import com.arandarkt.game.api.components.widgets.WidgetComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

class FixedGameFrame(override val player: PlayerCharacter) : WidgetComponent {
    override val widgetId: Int = 548



    override fun onButtonClicked(buttonId: Int, option: Int) {

    }

    override fun onOpen() {

    }

    override fun onClose() {

    }
}