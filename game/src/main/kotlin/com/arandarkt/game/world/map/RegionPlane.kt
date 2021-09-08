package com.arandarkt.game.world.map

import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.world.map.RegionFlags
import com.arandarkt.game.api.world.map.RegionPlane
import com.arandarkt.game.api.world.map.WorldRegion

class RegionPlane(override val plane: Int, override val parentRegion: WorldRegion) : RegionPlane {
    override val planeFlags: RegionFlags = RegionFlags(plane, parentRegion.baseLocation.position.x, parentRegion.baseLocation.position.y)
    override val projectileFlags: RegionFlags = RegionFlags(plane, parentRegion.baseLocation.position.x, parentRegion.baseLocation.position.y)
    override val npcs = mutableListOf<Character>()
    override val players = mutableListOf<PlayerCharacter>()
}