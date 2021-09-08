package com.arandarkt.game.api.world.map

import com.arandarkt.game.api.entity.`object`.GameObject
import com.arandarkt.game.api.world.location.Location
import com.arandarkt.game.api.world.map.items.RegionItemManager

interface RegionChunk {

    val rotation: Int

    val copiedBaseLocation: Location
    val baseLocation: Location
    val regionPlane: RegionPlane
    val itemManager: RegionItemManager

    val objects: Array<Array<GameObject?>>

}