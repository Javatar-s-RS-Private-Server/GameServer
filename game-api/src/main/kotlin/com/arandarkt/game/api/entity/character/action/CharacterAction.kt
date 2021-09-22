package com.arandarkt.game.api.entity.character.action

import com.arandarkt.game.api.components.ComponentManager
import com.arandarkt.game.api.entity.character.Character

interface CharacterAction {

    val components: ComponentManager<ActionComponent>
    var isActive: Boolean
    var isCompleted: Boolean
    var isCanceled: Boolean

    suspend fun fireAction(entity: Character) : Boolean {
        if(isActive && isCanceled) {
            isActive = false
        }
        if(isCompleted) {
            isActive = false
        }
        if(isActive && !isCompleted) {
            isCompleted = onAction(entity)
            if(isCompleted) {
                onCompleted(entity)
            }
        }
        return !isActive || isCompleted || isCanceled
    }

    fun cancel(entity: Character) {
        if (isActive) {
            isCanceled = true
            onCancel(entity)
        }
    }

    suspend fun onAction(entity: Character) : Boolean
    fun onCompleted(entity: Character) {}
    fun onCancel(entity: Character) {}
}