package com.arandarkt.game.world.map

import com.arandarkt.game.api.entity.Entity
import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.entity.components.player.ViewportComponent
import com.arandarkt.game.api.world.location.components.PositionComponent
import com.arandarkt.game.api.world.map.IRegionManager
import com.arandarkt.game.api.world.map.WorldRegion

class RegionManager : IRegionManager {
    override val regions = mutableMapOf<Int, Region>()
    override fun region(regionHash: Int): WorldRegion {
        return regions.getOrPut(regionHash) { Region(regionHash shr 8, regionHash and 255) }
    }

    override fun getLocalPlayers(entity: Entity, distance: Int): List<PlayerCharacter> {
        if(entity.components.hasComponent<ViewportComponent>()) {
            val players = mutableListOf<PlayerCharacter>()
            val viewport = entity.component<ViewportComponent>()
            for(plane in viewport.region.planes) {
                players.addAll(plane.players.filter { it.component<PositionComponent>().withinDistance(entity.component(), distance) })
            }
            return players
        }
        return emptyList()
    }

    override fun getLocalNpcs(entity: Entity, distance: Int): List<Character> {
        TODO("Not yet implemented")
    }

    override fun getClippingFlag(z: Int, x: Int, y: Int): Int {
        TODO("Not yet implemented")
    }
}