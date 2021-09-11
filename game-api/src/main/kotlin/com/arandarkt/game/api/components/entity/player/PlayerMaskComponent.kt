package com.arandarkt.game.api.components.entity.player

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.components.entity.flags.FlagComponent
import com.arandarkt.game.api.world.location.components.PositionComponent

class PlayerMaskComponent : Component {

    val isUpdateRequired: Boolean
        get() = maskData != 0

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
        maskData = maskData or flagComponent.flagId
    }

    fun markAsPlayerMask() {
        maskData = maskData or 0x10
    }

    fun markAsNpcMask() {
        maskData = maskData or 0x8
    }

    fun reset() {
        maskData = 0
        isTeleporting = false
    }

    override suspend fun onTick(currentTick: Long) {

    }

    companion object {
        const val SIZE = 11
    }

}