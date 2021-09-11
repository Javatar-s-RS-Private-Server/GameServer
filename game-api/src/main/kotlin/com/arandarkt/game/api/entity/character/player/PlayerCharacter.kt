package com.arandarkt.game.api.entity.character.player

import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.entity.widget.GameWidgetManager
import com.arandarkt.game.api.packets.GameSession

interface PlayerCharacter : Character {

    val session: GameSession
    val details: PlayerDetails
    val widgetManager: GameWidgetManager

    override var index: Int

    fun initialize()
    fun initializeComponents()
    fun logout()

    fun shouldShuffle(): Boolean

}