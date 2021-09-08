package com.arandarkt.game.world.map

import com.arandarkt.game.api.world.location.Location
import com.arandarkt.game.api.world.map.RegionPlane
import com.arandarkt.game.api.world.map.WorldRegion

class Region(override val regionX: Int, override val regionY: Int) : WorldRegion {

    override var isLoaded: Boolean = false
    override var hasFlags: Boolean = false
    override var isActive: Boolean = false
    override var isEditable: Boolean = false
    override var objectCount: Int = 0
        private set
    override val planes: Array<RegionPlane> = Array(4) { RegionPlane(it, this) }
    override var playerViewingCount: Int = 0

    override val baseLocation: Location
        get() = Location().position((regionX shl 6), (regionY shl 6), 0)

    override suspend fun onTick(currentTick: Long) {}

}