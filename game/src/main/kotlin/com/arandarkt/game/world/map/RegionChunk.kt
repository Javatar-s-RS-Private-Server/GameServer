package com.arandarkt.game.world.map

import com.arandarkt.game.api.entity.`object`.GameObject
import com.arandarkt.game.api.world.location.Location
import com.arandarkt.game.api.world.map.RegionChunk
import com.arandarkt.game.api.world.map.RegionPlane
import com.arandarkt.game.api.world.map.items.RegionItemManager

class RegionChunk(
    override val baseLocation: Location,
    override val rotation: Int,
    override val regionPlane: RegionPlane
) : RegionChunk {

    override val objects: Array<Array<GameObject?>> = Array(8) { Array(8) { null } }
    override var copiedBaseLocation: Location = baseLocation

    override val itemManager: RegionItemManager
        get() = TODO("Not yet implemented")

}