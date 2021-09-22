package com.arandarkt.game.api.entity.widget

import com.arandarkt.game.api.components.widgets.TabComponent
import com.arandarkt.game.api.components.widgets.WidgetComponent
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

interface GameWidgetManager {
    val player: PlayerCharacter

    val frame: WidgetComponent
    val widget: WidgetComponent?
    val tabs: GameTabManager

    fun openWindow(windowId: Int)
    fun openInViewport(widget: WidgetComponent)
    fun openTab(tab: TabComponent)
    fun open(windowChildId: Int, widgetId: Int, overlay: Boolean = false)
    fun close()

    fun handleFrameButtonClick(childId: Int)

    suspend fun onTick(currentTick: Long) {
        widget?.onTick(currentTick)

    }

    companion object {
        fun GameWidgetManager.hideTab() {

        }
    }
}