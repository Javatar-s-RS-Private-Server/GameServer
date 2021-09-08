package com.arandarkt.game.world.map

import com.arandarkt.game.api.world.map.RegionFlags

class RegionFlags(
    override val plane: Int,
    override val baseX: Int,
    override val baseY: Int,
    override val projectile: Boolean = false,
    override val members: Boolean = false
) : RegionFlags {

    override val clippingFlags: Array<IntArray> = Array(0) { IntArray(0) }
    override val landscape: Array<BooleanArray> = Array(0) { BooleanArray(0) }

}