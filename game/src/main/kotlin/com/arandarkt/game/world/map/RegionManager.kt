package com.arandarkt.game.world.map

import com.arandarkt.definitions.MapFloorDefinition
import com.arandarkt.definitions.managers.MapFloorManager
import com.arandarkt.definitions.managers.ObjectPlacementManager
import com.arandarkt.game.api.components.entity.player.ViewportComponent
import com.arandarkt.game.api.entity.Entity
import com.arandarkt.game.api.entity.`object`.GameObject
import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.entity.character.Character.Companion.isPlayer
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.koin.inject
import com.arandarkt.game.api.world.location.components.Position
import com.arandarkt.game.api.world.map.GameRegionManager
import com.arandarkt.game.api.world.map.MapRegion
import com.arandarkt.game.entity.`object`.WorldObject
import com.arandarkt.game.world.map.region.Region
import com.arandarkt.xtea.RegionKey
import com.arandarkt.xtea.XteaManager
import com.displee.cache.CacheLibrary
import io.netty.buffer.Unpooled

class RegionManager : GameRegionManager {
    override val regions = mutableMapOf<Int, MapRegion>()

    private val cache: CacheLibrary by inject()
    private val mapFloorManager: MapFloorManager by inject()
    private val objectsManager: ObjectPlacementManager by inject()
    private val xteaManager: XteaManager by inject()

    override fun region(regionHash: Int): MapRegion {
        return regions.getOrPut(regionHash) { loadRegion(regionHash) }
    }

    override fun region(x: Int, y: Int): MapRegion {
        return region(x shr 6 shl 8 or (y shr 6))
    }

    override fun getLocalPlayers(entity: Entity, distance: Int): List<PlayerCharacter> {
        if (entity.components.hasComponent<ViewportComponent>()) {
            val players = mutableListOf<PlayerCharacter>()
            val viewport = entity.component<ViewportComponent>()
            val entityPos = entity.component<Position>()
            val region = region(entityPos.getRegionId())

            for (player in region.players) {
                if (player == entity)
                    continue
                val otherPos = player.component<Position>()
                if (otherPos.withinDistance(entityPos, distance)) {
                    viewport.localPlayers.add(player)
                }
            }

            return players
        }
        return emptyList()
    }

    override fun getLocalNpcs(entity: Entity, distance: Int): List<Character> {
        TODO("Not yet implemented")
    }

    override fun getClippingFlag(z: Int, x: Int, y: Int): Int {
        return region(x shr 3, y shr 3).getClippingMask(x, y, z)
    }

    override fun getClippingFlag(pos: Position): Int {
        return region(pos.getRegionId()).getClippingMask(pos.x, pos.y, pos.z)
    }

    override fun loadRegion(regionId: Int): MapRegion {
        val rx = regionId shr 8
        val ry = regionId and 255

        val floorArchive = cache.index(5).archive("m${rx}_$ry")
        val groundBuffer = floorArchive?.file(0)?.data

        val floorDef = if (groundBuffer != null) {
            mapFloorManager.load0(Unpooled.wrappedBuffer(groundBuffer))
        } else null

        val regionKey = xteaManager.xteas.getOrDefault(regionId, RegionKey(regionId, intArrayOf(0, 0, 0, 0)))
        val landscapeArchive = cache.index(5).archive("l${rx}_$ry", intArrayOf(0, 0, 0, 0))
        val landscapeData = landscapeArchive?.file(0)?.data
        if(landscapeData == null) {
            System.err.println("Map $regionId is null.")
        }
        val objsDef = if (landscapeData != null && floorDef != null) {
            val objectBuf = Unpooled.wrappedBuffer(landscapeData)
            objectsManager.load0(objectBuf, floorDef)
        } else null

        val region = Region(regionId, floorDef?.landscape ?: emptyArray())

        if (floorDef != null) {
            clipFloors(region, floorDef)
        }

        if (objsDef != null) {
            val worldObjs = objsDef.objects.map {
                WorldObject(
                    it.objectId,
                    Position(rx + it.x, ry + it.y, it.height),
                    it.type,
                    it.rotation
                )
            }
            println("Adding ${worldObjs.size} to region $regionId")
            worldObjs.forEach {
                addObject(region, it)
            }
        }

        return region

    }

    private fun addObject(region: MapRegion, obj: GameObject) {
        region.objects.add(obj)
        region.addClipping(obj)
    }

    override fun addObject(obj: GameObject) {
        val pos = obj.component<Position>()
        addObject(region(pos.getRegionId()), obj)
    }

    override fun addPlayer(player: PlayerCharacter) {
        val pos = player.component<Position>()
        val region = region(pos.getRegionId())
        region.players.add(player)
    }

    override fun addNpc(npc: Character) {
        val pos = npc.component<Position>()
        val region = region(pos.getRegionId())
        region.npcs.add(npc)
    }

    override fun isTeleportPermitted(l: Position): Boolean {
        return isTeleportPermitted(l.z, l.x, l.y)
    }

    override fun movePlayer(character: Character) {
        val pos = character.component<Position>()
        val regionId: Int = pos.getRegionX() shr 3 shl 8 or (pos.getRegionY() shr 3)
        val viewport = character.component<ViewportComponent>()
        val current = region(regionId)
        viewport.localPlayers.remove(character)
        if (character.isPlayer) {
            current.players.add(character as PlayerCharacter)
        } else {
            current.npcs.add(character)
        }
        viewport.region = current
    }

    private fun isTeleportPermitted(z: Int, x: Int, y: Int): Boolean {
        if (!isLandscaped(z, x, y)) {
            return false
        }
        val flag: Int = getClippingFlag(z, x, y)
        return flag and 0x12c0102 == 0 || flag and 0x12c0108 == 0 || flag and 0x12c0120 == 0 || flag and 0x12c0180 == 0
    }

    fun isLandscaped(z: Int, x: Int, y: Int): Boolean {
        var x = x
        var y = y
        val region: MapRegion = region(x, y)
        if (region.landscape.isEmpty()) {
            return false
        }
        x -= x shr 6 shl 6
        y -= y shr 6 shl 6
        return region.landscape[z][x][y]
    }

    private fun clipFloors(r: Region, floor: MapFloorDefinition) {
        for (z in 0..3) {
            for (x in 0..63) {
                for (y in 0..63) {
                    val mapscape = floor.mapscape
                    if (mapscape[z][x][y].toInt() and 0x1 == 1) {
                        var plane = z
                        if (mapscape[1][x][y].toInt() and 0x2 == 2) {
                            plane--
                        }
                        if (plane > -1) {
                            r.addClipping(x + r.regionX, y + r.regionY, z, 0x200000)
                        }
                    }
                }
            }
        }
    }
}