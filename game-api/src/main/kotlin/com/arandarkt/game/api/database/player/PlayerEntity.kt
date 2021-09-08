package com.arandarkt.game.api.database.player

import com.arandarkt.game.api.entity.character.player.PlayerDetails
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PlayerEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, PlayerEntity>(PlayerTable)

    val username by PlayerTable.id
    var email by PlayerTable.email
    var password by PlayerTable.password
    var remoteHost by PlayerTable.remoteHost
    var prevRemoteHost by PlayerTable.previousRemoteHost
    var memberShipId by PlayerTable.memberShipId
    var role by PlayerTable.role
    var creationDate by PlayerTable.accountCreatedDate

    fun toPlayerDetails() = PlayerDetails(
        username.value,
        password,
        email,
        remoteHost,
        prevRemoteHost,
        memberShipId,
        role,
        creationDate
    )

}