package com.arandarkt.game.api.database.player

import com.arandarkt.game.api.database.EntityService
import com.arandarkt.game.api.entity.character.player.PlayerDetails
import com.arandarkt.game.api.entity.character.player.PlayerRole
import org.jetbrains.exposed.sql.transactions.transaction

class PlayerEntityService : EntityService<PlayerDetails, String> {
    override fun all(): Iterable<PlayerDetails> {
        return transaction {
            PlayerEntity.all().map(PlayerEntity::toPlayerDetails)
        }
    }

    override fun get(id: String): PlayerDetails {
        return transaction { PlayerEntity[id].toPlayerDetails() }
    }

    override fun add(value: PlayerDetails) {
        transaction {
            PlayerEntity.new(value.username) {
                this.email = value.email
                this.password = value.password
                this.remoteHost = value.remoteHost
                this.prevRemoteHost = value.prevRemoteHost
                this.memberShipId = value.memberShipId
                this.role = value.role.roleName
                this.creationDate = value.creationTime
            }
        }
    }

    override fun update(value: PlayerDetails) {
        transaction {
            with(PlayerEntity[value.username]) {
                email = value.email
                password = value.password
                remoteHost = value.remoteHost
                prevRemoteHost = value.prevRemoteHost
                memberShipId = value.memberShipId
                role = value.role.roleName
            }
        }
    }

    override fun delete(id: String) {
        transaction {
            PlayerEntity[id].delete()
        }
    }

    override fun getOrAdd(value: PlayerDetails): PlayerDetails {
        transaction {
            val user =  PlayerEntity.findById(value.username)
            if(user == null) {
                add(value)
                return@transaction value
            } else {
                return@transaction user
            }
        }
        return value
    }

    override fun exists(id: String): Boolean {
        return transaction { PlayerEntity.findById(id) } != null
    }
}