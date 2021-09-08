package com.arandarkt.game.api.entity.character.player.widget

interface ParentWidget {

    val id: Int
    val configuration: WidgetConfiguration

    fun onOpen()
    fun onClose()

}