package com.arandarkt.game.api.world.map

import com.arandarkt.game.api.koin.inject
import com.arandarkt.game.api.world.location.Location
import com.arandarkt.game.api.world.location.components.Position
import java.util.*
import kotlin.math.abs

enum class Direction(

    val stepX: Int,
    val stepY: Int,
    private val value: Int,
    vararg var traversal: Int
) {

    NORTH_WEST(-1, 1, 7, 0x12c0108, 0x12c0120, 0x12c0138),


    NORTH(0, 1, 0, 0x12c0120),

    NORTH_EAST(1, 1, 4, 0x12c0180, 0x12c0120, 0x12c01e0),


    WEST(-1, 0, 3, 0x12c0108),

    EAST(1, 0, 1, 0x12c0180),


    SOUTH_WEST(-1, -1, 6, 0x12c0108, 0x12c0102, 0x12c010e),


    SOUTH(0, -1, 2, 0x12c0102),


    SOUTH_EAST(1, -1, 5, 0x12c0180, 0x12c0102, 0x12c0183);

    val opposite: Direction
        get() = Companion[toInteger() + 2 and 3]
    private val regionManager: GameRegionManager by inject()
    fun toName(direction: Direction): String {
        return direction.name.lowercase(Locale.getDefault())
    }

    fun toInteger(): Int {
        return value
    }

    companion object {

        operator fun get(rotation: Int): Direction {
            for (dir in values()) {
                if (dir.value == rotation) {
                    return dir
                }
            }
            throw IllegalArgumentException("Invalid direction value - $rotation")
        }


        fun getDirection(diffX: Int, diffY: Int): Direction {
            if (diffX < 0) {
                if (diffY < 0) {
                    return SOUTH_WEST
                } else if (diffY > 0) {
                    return NORTH_WEST
                }
                return WEST
            } else if (diffX > 0) {
                if (diffY < 0) {
                    return SOUTH_EAST
                } else if (diffY > 0) {
                    return NORTH_EAST
                }
                return EAST
            }
            return if (diffY < 0) {
                SOUTH
            } else NORTH
        }

        fun forWalkFlag(walkingFlag: Int, rotation: Int): Direction? {
            var walking = walkingFlag
            if (rotation != 0) {
                walking = (walking shl rotation and 0xf) + (walking shr 4 - rotation)
            }
            if (walking > 0) {
                if (walking and 0x8 == 0) {
                    return WEST
                }
                if (walking and 0x2 == 0) {
                    return EAST
                }
                if (walking and 0x4 == 0) {
                    return SOUTH
                }
                if (walking and 0x1 == 0) {
                    return NORTH
                }
            }
            return null
        }

        fun getLogicalDirection(location: Position, l: Position): Direction {
            val offsetX = abs(l.x - location.x)
            val offsetY = abs(l.y - location.y)
            if (offsetX > offsetY) {
                return if (l.x > location.x) {
                    EAST
                } else {
                    WEST
                }
            } else if (l.y < location.y) {
                return SOUTH
            }
            return NORTH
        }
    }
}