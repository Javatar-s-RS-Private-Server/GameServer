package com.arandarkt.game.api.components.widgets

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

interface WidgetComponent : Component {

    val player: PlayerCharacter

    val widgetId: Int

    fun onButtonClicked(buttonId: Int, option: Int = 0)
    fun onOpen()
    fun onClose()

}