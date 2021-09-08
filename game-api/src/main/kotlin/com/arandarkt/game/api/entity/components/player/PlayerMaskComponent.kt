package com.arandarkt.game.api.entity.components.player

import com.arandarkt.game.api.entity.Component
import com.arandarkt.game.api.entity.components.flags.FlagComponent
import com.arandarkt.game.api.world.location.Location
import com.arandarkt.game.api.world.location.components.PositionComponent
import kotlin.reflect.KClass

class PlayerMaskComponent : Component {

    val isUpdateRequired: Boolean = false

    var maskData = 0
        private set
    var syncedMaskData = 0
        private set

    var lastSceneGraph: PositionComponent = PositionComponent(0, 0, 0)
    var shouldUpdateSceneGraph: Boolean = false
    var isTeleporting: Boolean = false

    val flags = mutableListOf<FlagComponent>()
    val syncedFlags = mutableListOf<Boolean>()

    fun with(flagComponent: FlagComponent) {
        flags.add(flagComponent)
    }

    override suspend fun onTick(currentTick: Long) {

    }

    companion object {
        const val SIZE = 11
    }

}