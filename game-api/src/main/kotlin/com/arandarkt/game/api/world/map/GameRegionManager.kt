package com.arandarkt.game.api.world.map

import com.arandarkt.game.api.entity.Entity
import com.arandarkt.game.api.entity.`object`.GameObject
import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.world.World
import com.arandarkt.game.api.world.location.components.Position

interface GameRegionManager {

    val regions: Map<Int, MapRegion>

    fun region(regionHash: Int): MapRegion
    fun region(x: Int, y: Int) : MapRegion

    fun getLocalPlayers(entity: Entity, distance: Int): List<PlayerCharacter>
    fun getLocalNpcs(entity: Entity, distance: Int): List<Character>
    fun getClippingFlag(z: Int, x: Int, y: Int): Int
    fun getClippingFlag(pos: Position) : Int

    fun loadRegion(regionId: Int) : MapRegion

    fun addObject(obj: GameObject)
    fun addPlayer(player: PlayerCharacter)
    fun addNpc(npc: Character)
    fun isTeleportPermitted(l: Position): Boolean

    fun movePlayer(character: Character)

}