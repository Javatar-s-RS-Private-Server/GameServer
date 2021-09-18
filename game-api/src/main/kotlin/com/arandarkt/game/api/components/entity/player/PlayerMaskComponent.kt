package com.arandarkt.game.api.components.entity.player

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.components.entity.flags.FlagComponent
import com.arandarkt.game.api.world.location.components.Position

class PlayerMaskComponent : Component {

    val isUpdateRequired: Boolean
        get() = maskData != 0

    var maskData = 0
        private set
    var syncedMaskData = 0
        private set

    var lastSceneGraph: Position = Position(0, 0, 0)
    var shouldUpdateSceneGraph: Boolean = false

    val flags = mutableListOf<FlagComponent>()
    val syncedFlags = mutableListOf<Boolean>()

    fun with(flagComponent: FlagComponent) {
        if (!flags.contains(flagComponent)) {
            flags.add(flagComponent)
        }
        maskData = maskData or flagComponent.flagId
    }

    fun markAsPlayerMask() {
        maskData = maskData or 0x10
    }

    fun markAsNpcMask() {
        maskData = maskData or 0x8
    }

    fun reset() {
        if (maskData != 0) {
            maskData = 0
        }
    }

    override suspend fun onTick(currentTick: Long) {

    }

    companion object {
        const val SIZE = 11
    }

}