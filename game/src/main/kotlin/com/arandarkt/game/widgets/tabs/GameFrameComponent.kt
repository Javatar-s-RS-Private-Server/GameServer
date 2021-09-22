package com.arandarkt.game.widgets.tabs

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.components.widgets.WidgetType
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

class GameFrameComponent(val player: PlayerCharacter) : Component {

    fun openFrame() = apply {
        player.widgetManager.openWindow(548)
    }

    fun openChat() = apply {
        player.widgetManager.open(WidgetType.CHATBOX.fixedChildId, 137, true)
    }

    fun openDefaultTabs() = apply {
        with(player.widgetManager) {
            openTab(AttackTab(player))
            openTab(SkillsTab(player))
            openTab(QuestTab(player))
            openTab(InventoryTab(player))
            openTab(EquipmentTab(player))
            openTab(PrayerTab(player))
            openTab(MagicTab(player))
            openTab(ClanChatTab(player))
            openTab(FriendsTab(player))
            openTab(IgnoreTab(player))
            openTab(LogoutTab(player))
            openTab(SettingsTab(player))
            openTab(EmoteTab(player))
            openTab(MusicTab(player))
        }
    }

}