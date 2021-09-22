package com.arandarkt.game.api.entity.character

import com.arandarkt.game.api.entity.Entity
import com.arandarkt.game.api.entity.character.action.CharacterAction
import com.arandarkt.game.api.entity.hasComponent
import com.arandarkt.game.api.packets.GameSession

interface Character : Entity {

    val index: Int

    companion object {
        val Character.isPlayer get() = hasComponent<GameSession>()
    }

}