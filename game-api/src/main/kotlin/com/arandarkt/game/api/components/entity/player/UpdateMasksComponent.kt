package com.arandarkt.game.api.components.entity.player

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.components.entity.flags.FlagComponent
import com.arandarkt.game.api.world.location.components.Position
import kotlin.reflect.KClass

class UpdateMasksComponent : Component {

    val isUpdateRequired: Boolean
        get() = maskData != 0

    var maskData = 0
        private set

    var lastSceneGraph: Position = Position.VOID_LOCATION
    var shouldUpdateSceneGraph: Boolean = false

    val flags = mutableMapOf<KClass<*>, FlagComponent>()

    fun with(flagComponent: FlagComponent) {
        if (!flags.containsKey(flagComponent::class)) {
            flags[flagComponent::class] = flagComponent
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
        shouldUpdateSceneGraph = false
    }
}