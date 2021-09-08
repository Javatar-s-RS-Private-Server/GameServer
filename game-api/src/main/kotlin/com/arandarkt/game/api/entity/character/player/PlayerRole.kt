package com.arandarkt.game.api.entity.character.player

enum class PlayerRole(val roleName: String, val id: Int) {

    PLAYER("player", 0),
    MODERATOR("moderator", 1),
    ADMIN("admin", 2);

    companion object {

        val roles = values()

        fun findRoleByName(roleName: String): PlayerRole {
            val role = roles.find { it.roleName == roleName }
            return role ?: error("Role not found $roleName")
        }

    }

}