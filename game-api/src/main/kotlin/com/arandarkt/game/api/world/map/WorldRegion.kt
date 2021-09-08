package com.arandarkt.game.api.world.map

import com.arandarkt.game.api.world.location.Location

interface WorldRegion {

    val regionX: Int
    val regionY: Int
    val isActive: Boolean

    val objectCount: Int
    val hasFlags: Boolean
    val isLoaded: Boolean
    val playerViewingCount: Int
    val isEditable: Boolean

    val baseLocation: Location
    val planes: Array<RegionPlane>

    suspend fun onTick(currentTick: Long)

}