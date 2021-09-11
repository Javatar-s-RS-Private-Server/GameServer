package com.arandarkt.game.api.entity.widget

import com.arandarkt.game.api.components.widgets.TabComponent

interface GameTabManager {

    val tabComponents: Array<TabComponent?>

    fun select(tabId: Int) : Int
    fun unselect()

    fun onButtonClicked(buttonId: Int, option: Int)

    fun isTabSelected(widgetId: Int): Boolean

    suspend fun onTick(currentTick: Long)
}