package com.arandarkt.game.entity.character.widget

import com.arandarkt.game.api.components.widgets.TabComponent
import com.arandarkt.game.api.entity.widget.GameTabManager

class TabManager : GameTabManager {
    override val tabComponents: Array<TabComponent?> = arrayOfNulls(14)

    private var selectedTab = -1

    override fun select(tabId: Int) : Int {
        if (tabComponents[tabId] != null) {
            val prevSelected = selectedTab
            selectedTab = tabId
            return prevSelected
        }
        return -1
    }

    override fun unselect() {
        selectedTab = -1
    }

    override fun onButtonClicked(buttonId: Int, option: Int) {
        if (selectedTab != -1) {
            tabComponents[selectedTab]?.onButtonClicked(buttonId, option)
        }
    }

    override suspend fun onTick(currentTick: Long) {
        if(selectedTab != -1) {
            tabComponents[selectedTab]?.onTick(currentTick)
        }
    }

    override fun isTabSelected(widgetId: Int): Boolean {
        if(selectedTab == -1)
            return false
        if(tabComponents[selectedTab] == null)
            return false
        return tabComponents[selectedTab]?.widgetId == widgetId
    }
}