package com.arandarkt.game.api.entity.character.player

import java.time.LocalDateTime

class PlayerDetails(
    val username: String,
    val password: String,
    val email: String,
    val remoteHost: String,
    val prevRemoteHost: String,
    val memberShipId: String,
    role: String,
    val creationTime: LocalDateTime
) {

    val role: PlayerRole = PlayerRole.findRoleByName(role)

}