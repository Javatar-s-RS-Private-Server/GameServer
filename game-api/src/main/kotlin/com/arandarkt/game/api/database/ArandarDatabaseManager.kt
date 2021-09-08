package com.arandarkt.game.api.database

import com.arandarkt.game.api.database.player.PlayerTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.FileInputStream
import java.util.*

class ArandarDatabaseManager {

    private val props = Properties()

    fun connect() {

        props.load(FileInputStream("/home/javatar/IdeaProjects/ArandarKt/game-api/src/main/resources/db.properties"))

        val user = props.getProperty("user")
        val pass = props.getProperty("pass")

        Database.connect(
            "jdbc:pgsql://192.168.0.185:5432/arandar",
            user = user,
            password = pass
        )

        transaction {
            SchemaUtils.createMissingTablesAndColumns(PlayerTable)
        }

    }

}