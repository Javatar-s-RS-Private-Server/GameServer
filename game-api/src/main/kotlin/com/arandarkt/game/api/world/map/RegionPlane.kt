package com.arandarkt.game.api.world.map

import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.entity.character.player.PlayerCharacter

interface RegionPlane {

    val plane: Int

    val parentRegion: WorldRegion
    val planeFlags: RegionFlags
    val projectileFlags: RegionFlags
    val players: List<PlayerCharacter>
    val npcs: List<Character>

}