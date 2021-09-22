package com.arandarkt.game.api.world.map

import com.arandarkt.game.api.entity.`object`.GameObject
import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.world.location.components.Position
import java.util.*
import kotlin.collections.HashSet

interface MapRegion {
    val regionId: Int
    val regionX: Int
    val regionY: Int
    val players: LinkedList<PlayerCharacter>
    val npcs: LinkedList<Character>
    val objects: LinkedList<GameObject>
    val clippingMasks: Array<Array<IntArray>>
    val isClipped: Boolean
    val landscape: Array<Array<BooleanArray>>

    val isActive: Boolean
        get() = players.size > 0

    fun addClipping(obj: GameObject)
    fun removeClipping(obj: GameObject)
    fun addClipping(x: Int, y: Int, z: Int, mask: Int)
    fun setClipping(x: Int, y: Int, z: Int, mask: Int)
    fun removeClipping(x: Int, y: Int, z: Int, mask: Int)
    fun getClippingMask(x: Int, y: Int, z: Int): Int
    fun isPassable(l: Position): Boolean
    fun removeClippingForVariableObject(x: Int, y: Int, z: Int, type: Int, direction: Int, flag: Boolean)
    fun getClipping(x: Int, y: Int, z: Int, moveTypeX: Int, moveTypeY: Int): Boolean
    fun blockedNorth(loc: Position): Boolean
    fun blockedEast(loc: Position): Boolean
    fun blockedSouth(loc: Position): Boolean
    fun blockedWest(loc: Position): Boolean
    fun blockedNorthEast(loc: Position): Boolean
    fun blockedNorthWest(loc: Position): Boolean
    fun blockedSouthEast(loc: Position): Boolean
    fun blockedSouthWest(loc: Position): Boolean
}