package com.arandarkt.game.entity.character.player.widget

import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.character.player.widget.*
import com.arandarkt.game.api.entity.character.player.widget.WidgetManager
import com.arandarkt.game.api.packets.GameSession.Companion.sendPacket
import com.arandarkt.game.api.packets.outgoing.OpenWidgetPacket
import kotlinx.coroutines.flow.MutableStateFlow

class WidgetManager(val player: PlayerCharacter) : WidgetManager {
    override val gameframe: GameFrameWidget = GameFrameWidgetImpl()
    override val gameTab: TabWidget = TabWidgetImpl(0, EmptyConfiguration)
    override val chatbox: ChatWidget = ChatWidgetImpl(1, EmptyConfiguration)

    override val frameWidget: MutableStateFlow<GameWidget?> = MutableStateFlow(null)

    override fun open(widget: GameWidget) {
        widget.onOpen()
        frameWidget.value = widget
    }

    override fun close(widget: GameWidget) {
        widget.onClose()
        frameWidget.value = null
    }

}