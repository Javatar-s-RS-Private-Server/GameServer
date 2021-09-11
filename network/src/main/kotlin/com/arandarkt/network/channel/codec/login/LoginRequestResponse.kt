package com.arandarkt.network.channel.codec.login

import com.arandarkt.game.api.entity.character.player.PlayerCharacter

class LoginRequestResponse(
    val sessionKey: Long,
    val response: LoginResponse,
    val player: PlayerCharacter? = null
)