package com.arandarkt.game.api.entity.character.player.widget

import kotlinx.coroutines.flow.MutableStateFlow

interface WidgetManager {

    val gameframe: GameFrameWidget
    val gameTab: TabWidget
    val chatbox: ChatWidget

    val frameWidget: MutableStateFlow<GameWidget?>

    fun open(widget: GameWidget)
    fun close(widget: GameWidget)

}