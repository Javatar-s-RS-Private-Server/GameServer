package com.arandarkt.game.api.database.player

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.`java-time`.CurrentDateTime
import org.jetbrains.exposed.sql.`java-time`.datetime

object PlayerTable : IdTable<String>() {

    val password = text("password")
    val email = text("email").default("none")
    val remoteHost = text("remote_host").default("unknown")
    val previousRemoteHost = text("prev_remote_host").default("unknown")
    val memberShipId = text("membership_id").default("none")
    val role = text("role").default("player")
    val accountCreatedDate = datetime("account_creation_date").defaultExpression(CurrentDateTime())

    override val id: Column<EntityID<String>> = varchar("username", 255).entityId()
}