package com.arandarkt.game.api.world.map

interface RegionFlags {

    val plane: Int

    val baseX: Int

    val baseY: Int

    val members: Boolean

    val clippingFlags: Array<IntArray>

    val landscape: Array<BooleanArray>

    val projectile: Boolean

}