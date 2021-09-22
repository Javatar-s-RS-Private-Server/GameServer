package com.arandarkt.game.api.world.map.path

import com.arandarkt.game.api.world.map.Direction
import kotlin.jvm.JvmOverloads

/**
 * Represents a point.
 * @author Emperor
 */
class Point @JvmOverloads constructor(
    val x: Int,
    val y: Int,
    val direction: Direction? = null,
    val diffX: Int = 0,
    val diffY: Int = 0
) {
    var isRunDisabled = false

    constructor(
        x: Int,
        y: Int,
        direction: Direction?,
        diffX: Int,
        diffY: Int,
        runDisabled: Boolean
    ) : this(
        x, y,
        direction,
        diffX,
        diffY
    ) {
        isRunDisabled = runDisabled
    }

    override fun toString(): String {
        return "Point(x=$x, y=$y, direction=$direction, diffX=$diffX, diffY=$diffY, isRunDisabled=$isRunDisabled)"
    }


}