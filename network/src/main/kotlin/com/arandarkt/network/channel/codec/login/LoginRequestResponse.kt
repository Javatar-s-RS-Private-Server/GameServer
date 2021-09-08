package com.arandarkt.network.channel.codec.login

import com.arandarkt.game.api.entity.character.player.PlayerCharacter

class LoginRequestResponse(
    val sessionKey: Long,
    val player: PlayerCharacter,
    val response: LoginResponse
)