package com.arandarkt.game.world.map

import com.arandarkt.definitions.MapFloorDefinition
import com.arandarkt.definitions.managers.MapFloorManager
import com.arandarkt.definitions.managers.ObjectPlacementManager
import com.arandarkt.game.api.components.entity.player.ViewportComponent
import com.arandarkt.game.api.entity.Entity
import com.arandarkt.game.api.entity.`object`.GameObject
import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.koin.inject
import com.arandarkt.game.api.world.location.components.Position
import com.arandarkt.game.api.world.map.GameRegionManager
import com.arandarkt.game.api.world.map.MapRegion
import com.arandarkt.game.entity.`object`.WorldObject
import com.arandarkt.game.world.map.region.Region
import com.displee.cache.CacheLibrary
import io.netty.buffer.Unpooled

class RegionManager : GameRegionManager {
    override val regions = mutableMapOf<Int, MapRegion>()

    private val cache: CacheLibrary by inject()
    private val mapFloorManager: MapFloorManager by inject()
    private val objectsManager: ObjectPlacementManager by inject()

    override fun region(regionHash: Int): MapRegion {
        return regions.getOrPut(regionHash) { loadRegion(regionHash) }
    }

    override fun region(x: Int, y: Int): MapRegion {
        return region(x + (y shl 8))
    }

    override fun getLocalPlayers(entity: Entity, distance: Int): List<PlayerCharacter> {
        if (entity.components.hasComponent<ViewportComponent>()) {
            val players = mutableListOf<PlayerCharacter>()
            val viewport = entity.component<ViewportComponent>()
            val entityPos = entity.component<Position>()
            val region = region(entityPos.getRegionId())

            for (player in region.players) {
                val otherPos = player.component<Position>()
                if(otherPos.withinDistance(entityPos, distance)) {
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

    override fun loadRegion(regionId: Int): MapRegion {
        val rx = regionId shr 8
        val ry = regionId and 255
        val region = Region(rx, ry)

        val groundBuffer = cache.data(5, "m${rx}_$ry")

        val floorDef = if (groundBuffer != null) {
            mapFloorManager.load0(Unpooled.wrappedBuffer(groundBuffer))
        } else null

        val landscapeData = cache.data(5, "l${rx}_$ry")
        val objsDef = if(landscapeData != null && floorDef != null) {
            val objectBuf = Unpooled.wrappedBuffer(landscapeData)
            objectsManager.load0(objectBuf, floorDef)
        } else null

        if(floorDef != null) {
            clipFloors(region, floorDef)
        }

        if(objsDef != null) {
            val worldObjs = objsDef.objects.map { WorldObject(
                it.objectId,
                Position(rx + it.x, ry + it.y, it.height),
                it.type,
                it.rotation
            ) }
            worldObjs.forEach { addObject(region, it) }
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
                            r.addClipping(x+r.regionX, y+r.regionY, z, 0x200000)
                        }
                    }
                }
            }
        }
    }
}