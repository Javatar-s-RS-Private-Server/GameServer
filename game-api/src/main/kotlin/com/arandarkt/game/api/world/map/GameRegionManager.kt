package com.arandarkt.game.api.world.map

import com.arandarkt.game.api.entity.Entity
import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

interface GameRegionManager {

    val regions: Map<Int, WorldRegion>

    fun region(regionHash: Int): WorldRegion

    fun getLocalPlayers(entity: Entity, distance: Int): List<PlayerCharacter>
    fun getLocalNpcs(entity: Entity, distance: Int): List<Character>
    fun getClippingFlag(z: Int, x: Int, y: Int): Int

}