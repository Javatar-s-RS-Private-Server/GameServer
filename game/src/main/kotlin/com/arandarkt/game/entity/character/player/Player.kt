package com.arandarkt.game.entity.character.player

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.components.ComponentManager
import com.arandarkt.game.api.components.entity.flags.player.AppearanceFlag
import com.arandarkt.game.api.components.entity.player.*
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.character.player.PlayerDetails
import com.arandarkt.game.api.components.entity.player.apperance.AppearanceComponent
import com.arandarkt.game.api.components.entity.player.skills.SkillsComponent
import com.arandarkt.game.api.components.widgets.WidgetType
import com.arandarkt.game.api.components.widgets.tabs.StatsTab
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.entity.getOrCreate
import com.arandarkt.game.api.entity.widget.GameWidgetManager
import com.arandarkt.game.api.entity.widget.LegacyWidgetActionHandler
import com.arandarkt.game.api.entity.with
import com.arandarkt.game.api.packets.GameSession
import com.arandarkt.game.api.packets.GameSession.Companion.sendPacket
import com.arandarkt.game.api.packets.incoming.Command
import com.arandarkt.game.api.packets.incoming.widgets.decoders.WidgetActionOne
import com.arandarkt.game.api.packets.incoming.widgets.decoders.WidgetActionTwo
import com.arandarkt.game.api.packets.outgoing.ClientScript
import com.arandarkt.game.api.packets.outgoing.GameMessage
import com.arandarkt.game.api.packets.outgoing.PlayerUpdates
import com.arandarkt.game.api.packets.outgoing.SmallVarbit
import com.arandarkt.game.api.world.location.components.PositionComponent
import com.arandarkt.game.entity.character.AbstractCharacter
import com.arandarkt.game.entity.character.widget.WidgetManager
import com.arandarkt.game.world.commands.CommandManager
import com.arandarkt.game.world.commands.HelloWorldCommand
import com.arandarkt.game.world.map.Region

class Player(override val details: PlayerDetails, override val session: GameSession) : AbstractCharacter(), PlayerCharacter {

    override var index: Int = -1
    override val components = ComponentManager<Component>()
    override val widgetManager: GameWidgetManager = WidgetManager(this)

    override fun initialize() {

        subscribePackets()

        val masks = component<PlayerMaskComponent>()
        masks.with(AppearanceFlag(this))

        widgetManager.openWindow(548)
        widgetManager.open(WidgetType.CHATBOX.fixedChildId, 137, true)
        widgetManager.openTab(StatsTab(this))

        session.sendPacket(SmallVarbit(334, 1))
        session.sendPacket(ClientScript(101, ""))
        session.sendPacket(GameMessage("Welcome to Arandar."))
    }

    override fun initializeComponents() {
        with(SerializationComponent(components))
        val position = getOrCreate { PositionComponent() }
        with(PlayerMaskComponent())
        with(ViewportComponent(Region(position.getRegionX(), position.getRegionY())))
        with(MovementComponent())
        with(ItemContainerComponent())
        with(AppearanceComponent(this))
        with(SkillsComponent())
        with(CommandManager(this))
    }

    override fun logout() {
        session.disconnect()
    }

    override fun shouldShuffle(): Boolean {
        return true
    }


    override suspend fun onTick(tick: Long) {
        for (component in components) {
            component.onTick(tick)
        }
        session.sendPacket(PlayerUpdates(this))
        component<PlayerMaskComponent>().reset()
        session.flush()
    }

    private fun subscribePackets() {
        with(component<CommandManager>()) {
            addCommand(HelloWorldCommand())
            session.handlePacket(Command, this)
        }
        val handler = LegacyWidgetActionHandler(this)
        session.handlePacket(WidgetActionOne, handler)
        session.handlePacket(WidgetActionTwo, handler)
    }

}