package com.arandarkt.game.entity.character.player

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.components.ComponentManager
import com.arandarkt.game.api.components.entity.flags.player.AppearanceFlag
import com.arandarkt.game.api.components.entity.player.*
import com.arandarkt.game.api.components.entity.player.apperance.AppearanceComponent
import com.arandarkt.game.api.components.entity.player.skills.SkillsComponent
import com.arandarkt.game.api.entity.Entity
import com.arandarkt.game.api.entity.bind
import com.arandarkt.game.api.entity.character.action.CharacterAction
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.character.player.PlayerDetails
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.entity.widget.GameWidgetManager
import com.arandarkt.game.api.entity.with
import com.arandarkt.game.api.koin.inject
import com.arandarkt.game.api.packets.GameSession
import com.arandarkt.game.api.packets.GameSession.Companion.sendPacket
import com.arandarkt.game.api.packets.incoming.Command
import com.arandarkt.game.api.packets.incoming.walking.MovementHandler
import com.arandarkt.game.api.packets.incoming.walking.decoders.ViewportMovement
import com.arandarkt.game.api.packets.outgoing.GameMessage
import com.arandarkt.game.api.packets.outgoing.PlayerUpdates
import com.arandarkt.game.api.world.location.components.Position
import com.arandarkt.game.api.world.map.GameRegionManager
import com.arandarkt.game.entity.character.AbstractCharacter
import com.arandarkt.game.entity.character.widget.WidgetManager
import com.arandarkt.game.widgets.tabs.GameFrameComponent
import com.arandarkt.game.world.commands.CommandManager
import com.arandarkt.game.world.commands.HelloWorldCommand
import com.arandarkt.game.world.commands.widget.OpenWidgetCommand
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.reflect.KClass

class Player(override val details: PlayerDetails) : AbstractCharacter(),
    PlayerCharacter {

    override var index: Int = -1
    override val components = ComponentManager<Component>()
    override val widgetManager: GameWidgetManager = WidgetManager(this)
    override val worldSize: Int = 1
    private val action: MutableStateFlow<CharacterAction?> = MutableStateFlow(null)
    private val session: GameSession by lazy { component() }
    private val movement: MovementComponent by lazy { component() }
    private val masks: UpdateMasksComponent by lazy { component() }
    private val regionManager: GameRegionManager by inject()

    override fun startAction(action: CharacterAction) {
        with(this.action) {
            value?.cancel(this@Player)
            value = action.also { it.isActive = true }
        }
    }

    override fun onLogin() {
        val session = component<GameSession>()
        val masks = component<UpdateMasksComponent>()
        masks.with(AppearanceFlag(this))

        component<GameFrameComponent>()
            .openFrame()
            .openChat()
            .openDefaultTabs()

        //session.sendPacket(SmallVarbit(334, 1))
        //session.sendPacket(ClientScript(101, ""))
        session.sendPacket(GameMessage("Welcome to Arandar."))
    }

    override fun initializePlayer(session: GameSession) {
        with(session) bind GameSession::class
        with(SerializationComponent(components))
        val pos = with { Position() }
        with(UpdateMasksComponent())
        with(ViewportComponent(this, regionManager.region(pos.getRegionId())))
        with(MovementComponent(this))
        with(ItemContainerComponent())
        with(AppearanceComponent(this))
        with(SkillsComponent())
        with(CommandManager(this))
        with(GameFrameComponent(this))
        subscribePackets(session)
    }

    override fun logout() {
        component<GameSession>().disconnect()
    }

    override fun shouldShuffle(): Boolean {
        return true
    }


    override suspend fun onTick(tick: Long) {
        val iter = components.components.iterator()
        while(iter.hasNext()) {
            val (_, comp) = iter.next()
            if(comp.onTick(tick)) {
                iter.remove()
            }
        }
        val action = action.value
        if(action != null && action.fireAction(this)) {
            this.action.value = null
        }
        movement.updateMovement()
        session.sendPacket(PlayerUpdates(this))
        masks.reset()
        movement.isTeleporting = false
    }

    private fun subscribePackets(session: GameSession) {
        with(component<CommandManager>()) {
            addCommand(HelloWorldCommand())
            addCommand(OpenWidgetCommand())
            session.handlePacket(Command, this)
        }
        val movementHandler = MovementHandler(this)
        session.handlePacket(ViewportMovement, movementHandler)
        /*val handler = LegacyWidgetActionHandler(this)
        session.handlePacket(WidgetActionOne, handler)
        session.handlePacket(WidgetActionTwo, handler)
        val movementHandler = MovementHandler(this)
        session.handlePacket(ViewportMovement, movementHandler)
        session.handlePacket(MinimapMovement, movementHandler)*/
    }

}