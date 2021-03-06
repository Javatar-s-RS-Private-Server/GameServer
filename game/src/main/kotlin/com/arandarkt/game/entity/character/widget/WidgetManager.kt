package com.arandarkt.game.entity.character.widget

import com.arandarkt.game.api.components.widgets.TabComponent
import com.arandarkt.game.api.components.widgets.WidgetComponent
import com.arandarkt.game.api.components.widgets.frame.FixedGameFrame
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.entity.widget.GameTabManager
import com.arandarkt.game.api.entity.widget.GameWidgetManager
import com.arandarkt.game.api.packets.GameSession
import com.arandarkt.game.api.packets.GameSession.Companion.sendPacket
import com.arandarkt.game.api.packets.outgoing.CloseWidget
import com.arandarkt.game.api.packets.outgoing.OpenWidget
import com.arandarkt.game.api.packets.outgoing.OpenWindow

class WidgetManager(override val player: PlayerCharacter) : GameWidgetManager {

    private var openWindow: Int = -1
    private var openWidget: Int = -1
    override var widget: WidgetComponent? = null
        private set
    override val tabs: GameTabManager = TabManager()
    override val frame: WidgetComponent = FixedGameFrame(player)

    override fun openWindow(windowId: Int) {
        openWindow = windowId
        if (openWindow != -1) {
            player.component<GameSession>().sendPacket(OpenWindow(windowId))
        }
    }

    override fun openInViewport(widget: WidgetComponent) {
        this.widget = widget
        open(11, widget.widgetId)
    }

    override fun openTab(tab: TabComponent) {
        val prevTab = tabs.select(tab.tabId)
        if(prevTab != -1) {
            tabs.tabComponents[prevTab]?.onClose()
        }
        if(tabs.tabComponents[tab.tabId] == null) {
            tabs.tabComponents[tab.tabId] = tab
        }
        tabs.tabComponents[tab.tabId]?.onOpen()
        open(tab.tabIcon, tab.widgetId, true)
    }

    override fun open(windowChildId: Int, widgetId: Int, overlay: Boolean) {
        player.component<GameSession>().sendPacket(OpenWidget(openWindow, windowChildId, widgetId, overlay))
    }

    override fun close() {
        if (openWindow != -1 && openWidget != -1) {
            player.component<GameSession>().sendPacket(CloseWidget(openWindow, openWidget))
            widget?.onClose()
        }
    }

    override fun handleFrameButtonClick(childId: Int) {
        frame.onButtonClicked(childId)
    }
}