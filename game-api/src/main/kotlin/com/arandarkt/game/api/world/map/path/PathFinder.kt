package com.arandarkt.game.api.world.map.path

import com.arandarkt.game.api.world.location.components.Position

interface PathFinder {

    fun findPath(
        start: Position,
        moverSize: Int,
        end: Position,
        sizeX: Int,
        sizeY: Int,
        rotation: Int,
        type: Int,
        walkingFlag: Int,
        near: Boolean
    ): Path

    fun isStandingIn(
        x: Int,
        y: Int,
        moverSizeX: Int,
        moverSizeY: Int,
        destX: Int,
        destY: Int,
        sizeX: Int,
        sizeY: Int
    ): Boolean

    fun canInteract(
        x: Int,
        y: Int,
        moverSize: Int,
        destX: Int,
        destY: Int,
        sizeX: Int,
        sizeY: Int,
        walkFlag: Int,
        z: Int
    ): Boolean

    fun canInteractSized(
        curX: Int,
        curY: Int,
        moverSizeX: Int,
        moverSizeY: Int,
        destX: Int,
        destY: Int,
        sizeX: Int,
        sizeY: Int,
        walkingFlag: Int,
        z: Int
    ): Boolean

    fun canDoorInteract(
        curX: Int,
        curY: Int,
        size: Int,
        destX: Int,
        destY: Int,
        type: Int,
        rotation: Int,
        z: Int
    ): Boolean

    fun canDecorationInteract(
        curX: Int,
        curY: Int,
        size: Int,
        destX: Int,
        destY: Int,
        rotation: Int,
        type: Int,
        z: Int
    ) : Boolean

    fun check(x: Int, y: Int, direction: Int, currentCost: Int)
    fun reset()

    companion object {
        const val SOUTH_FLAG = 0x1

        /**
         * The west direction flag.
         */
        const val WEST_FLAG = 0x2

        /**
         * The north direction flag.
         */
        const val NORTH_FLAG = 0x4

        /**
         * The east direction flag.
         */
        const val EAST_FLAG = 0x8

        /**
         * The south-west direction flag.
         */
        const val SOUTH_WEST_FLAG = SOUTH_FLAG or WEST_FLAG

        /**
         * The north-west direction flag.
         */
        const val NORTH_WEST_FLAG = NORTH_FLAG or WEST_FLAG

        /**
         * The south-east direction flag.
         */
        const val SOUTH_EAST_FLAG = SOUTH_FLAG or EAST_FLAG

        /**
         * The north-east direction flag.
         */
        const val NORTH_EAST_FLAG = NORTH_FLAG or EAST_FLAG
    }
}