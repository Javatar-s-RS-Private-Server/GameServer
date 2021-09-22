package com.arandarkt.game.api.entity.character.player

import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.entity.character.action.CharacterAction
import com.arandarkt.game.api.entity.widget.GameWidgetManager
import com.arandarkt.game.api.packets.GameSession

interface PlayerCharacter : Character {

    val details: PlayerDetails
    val widgetManager: GameWidgetManager
    override var index: Int

    fun startAction(action: CharacterAction)
    fun onLogin()
    fun initializePlayer(session: GameSession)
    fun logout()

    fun shouldShuffle(): Boolean
}