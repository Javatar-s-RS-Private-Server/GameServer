package com.arandarkt.game.api.entity.character.player

import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.packets.GameSession

interface PlayerCharacter : Character {

    val session: GameSession
    val details: PlayerDetails

    override var index: Int

    fun initialize()
    fun logout()

    fun shouldShuffle(): Boolean

}