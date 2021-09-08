package com.arandarkt.game.entity.character.player

import com.arandarkt.game.api.entity.ComponentManager
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.character.player.PlayerDetails
import com.arandarkt.game.api.entity.character.player.PlayerRole
import com.arandarkt.game.api.entity.components.player.*
import com.arandarkt.game.api.entity.components.player.apperance.AppearanceComponent
import com.arandarkt.game.api.entity.getOrCreate
import com.arandarkt.game.api.entity.with
import com.arandarkt.game.api.packets.GameSession
import com.arandarkt.game.api.packets.GameSession.Companion.sendPacket
import com.arandarkt.game.api.packets.outgoing.OpenWidgetFramePacket
import com.arandarkt.game.api.packets.outgoing.PlayerUpdates
import com.arandarkt.game.api.world.location.components.PositionComponent
import com.arandarkt.game.entity.character.AbstractCharacter
import com.arandarkt.game.world.map.Region

class Player(override val details: PlayerDetails, override val session: GameSession) : AbstractCharacter(), PlayerCharacter {

    override var index: Int = -1
    override val components = ComponentManager()

    override fun initialize() {
        with(SerializationComponent(components))

        //load player file!

        if(details.role === PlayerRole.ADMIN) {
            //Subscribe admin only packets eg CommandPacket
        }
        session.sendPacket(OpenWidgetFramePacket(548))
        //session.sendPacket(OpenWidgetPacket(548, 77, 137, true))
        val position = getOrCreate { PositionComponent() }
        with(PlayerMaskComponent())
        with(ViewportComponent(Region(position.getRegionX(), position.getRegionY())))
        with(WalkingComponent())
        with(ItemContainerComponent())
        with(AppearanceComponent(this))
        with(SkillsComponent())
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
        session.flush()
    }

}