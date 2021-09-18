package com.arandarkt.game.api.world.map.path.impl

import com.arandarkt.game.api.koin.inject
import com.arandarkt.game.api.world.location.components.Position
import com.arandarkt.game.api.world.map.GameRegionManager
import com.arandarkt.game.api.world.map.Point
import com.arandarkt.game.api.world.map.path.Path
import com.arandarkt.game.api.world.map.path.PathFinder
import com.arandarkt.game.api.world.map.path.PathFinder.Companion.EAST_FLAG
import com.arandarkt.game.api.world.map.path.PathFinder.Companion.NORTH_EAST_FLAG
import com.arandarkt.game.api.world.map.path.PathFinder.Companion.NORTH_FLAG
import com.arandarkt.game.api.world.map.path.PathFinder.Companion.NORTH_WEST_FLAG
import com.arandarkt.game.api.world.map.path.PathFinder.Companion.SOUTH_EAST_FLAG
import com.arandarkt.game.api.world.map.path.PathFinder.Companion.SOUTH_FLAG
import com.arandarkt.game.api.world.map.path.PathFinder.Companion.SOUTH_WEST_FLAG
import com.arandarkt.game.api.world.map.path.PathFinder.Companion.WEST_FLAG

class SmartPathFinder : PathFinder {

    private val manager: GameRegionManager by inject()

    /**
     * The x-queue.
     */
    private val queueX: IntArray = IntArray(4096)

    /**
     * The y-queue.
     */
    private val queueY: IntArray = IntArray(4096)

    /**
     * The "via" array.
     */
    private val via: Array<IntArray> = Array(104) { IntArray(104) }

    /**
     * The cost array.
     */
    private val cost: Array<IntArray> = Array(104) { IntArray(104) }

    /**
     * The current writing position.
     */
    private var writePathPosition = 0

    /**
     * The current x-coordinate.
     */
    private var curX = 0

    /**
     * The current y-coordinate.
     */
    private var curY = 0

    /**
     * The destination x-coordinate.
     */
    private var dstX = 0

    /**
     * The destination y-coordinate.
     */
    private var dstY = 0

    /**
     * If a path was found.
     */
    private var foundPath = false

    override fun findPath(
        start: Position,
        moverSize: Int,
        end: Position,
        sizeX: Int,
        sizeY: Int,
        rotation: Int,
        type: Int,
        walkingFlag: Int,
        near: Boolean
    ): Path {
        reset()
        val path = Path()
        for (x in 0..103) {
            for (y in 0..103) {
                via[x][y] = 0
                cost[x][y] = 99999999
            }
        }
        val z: Int = start.z
        val location = Position(start.getRegionX() - 6 shl 3, start.getRegionY() - 6 shl 3, z)
        curX = start.getSceneX()
        curY = start.getSceneY()
        dstX = end.getSceneX(start)
        dstY = end.getSceneY(start)
        var attempts = 0
        var readPosition = 0
        check(curX, curY, 99, 0)
        if (moverSize < 2) {
            checkSingleTraversal(end, sizeX, sizeY, type, rotation, walkingFlag, location)
        } else if (moverSize == 2) {
            checkDoubleTraversal(end, sizeX, sizeY, type, rotation, walkingFlag, location)
        } else {
            checkVariableTraversal(end, moverSize, sizeX, sizeY, type, rotation, walkingFlag, location)
        }
        if (!foundPath) {
            if (near) {
                var fullCost = 1000
                var thisCost = 100
                val depth = 10
                for (x in dstX - depth..dstX + depth) {
                    for (y in dstY - depth..dstY + depth) {
                        if (x >= 0 && y >= 0 && x < 104 && y < 104 && cost[x][y] < 100) {
                            var diffX = 0
                            if (x < dstX) {
                                diffX = dstX - x
                            } else if (x > dstX + sizeX - 1) {
                                diffX = x - (dstX + sizeX - 1)
                            }
                            var diffY = 0
                            if (y < dstY) {
                                diffY = dstY - y
                            } else if (y > dstY + sizeY - 1) {
                                diffY = y - (dstY + sizeY - 1)
                            }
                            val totalCost = diffX * diffX + diffY * diffY
                            if (totalCost < fullCost || totalCost == fullCost && cost[x][y] < thisCost) {
                                fullCost = totalCost
                                thisCost = cost[x][y]
                                curX = x
                                curY = y
                            }
                        }
                    }
                }
                if (fullCost == 1000) {
                    return path
                }
                path.shouldMoveNear = true
            }
        }
        readPosition = 0
        queueX[readPosition] = curX
        queueY[readPosition++] = curY
        var previousDirection: Int
        attempts = 0
        var directionFlag = via[curX][curY].also { previousDirection = it }
        while (curX != start.getSceneX() || curY != start.getSceneY()) {
            if (++attempts > queueX.size) {
                return path
            }
            if (directionFlag != previousDirection) {
                previousDirection = directionFlag
                queueX[readPosition] = curX
                queueY[readPosition++] = curY
            }
            if (directionFlag and WEST_FLAG != 0) {
                curX++
            } else if (directionFlag and EAST_FLAG != 0) {
                curX--
            }
            if (directionFlag and SOUTH_FLAG != 0) {
                curY++
            } else if (directionFlag and NORTH_FLAG != 0) {
                curY--
            }
            directionFlag = via[curX][curY]
        }
        val size = readPosition--
        var absX: Int = location.x + queueX[readPosition]
        var absY: Int = location.y + queueY[readPosition]
        path.points.add(Point(absX, absY))
        for (i in 1 until size) {
            readPosition--
            absX = location.x + queueX[readPosition]
            absY = location.y + queueY[readPosition]
            path.points.add(Point(absX, absY))
        }
        path.isSuccessful = true
        return path
    }

    private fun checkVariableTraversal(
        end: Position,
        size: Int,
        sizeX: Int,
        sizeY: Int,
        type: Int,
        rotation: Int,
        walkingFlag: Int,
        location: Position
    ) {
        var readPosition = 0
        val z: Int = location.z
        main@ while (writePathPosition != readPosition) {
            curX = queueX[readPosition]
            curY = queueY[readPosition]
            readPosition = readPosition + 1 and 0xfff
            if (curX == dstX && curY == dstY) {
                foundPath = true
                break
            }
            val absX: Int = location.x + curX
            val absY: Int = location.y + curY
            if (type != 0) {
                if ((type < 5 || type == 10) && canDoorInteract(
                        absX,
                        absY,
                        size,
                        end.x,
                        end.y,
                        type - 1,
                        rotation,
                        z
                    )
                ) {
                    foundPath = true
                    break
                }
                if (type < 10 && canDecorationInteract(
                        absX,
                        absY,
                        size,
                        end.x,
                        end.y,
                        type - 1,
                        rotation,
                        z
                    )
                ) {
                    foundPath = true
                    break
                }
            }
            if (sizeX != 0 && sizeY != 0 && canInteract(
                    absX,
                    absY,
                    size,
                    end.x,
                    end.y,
                    sizeX,
                    sizeY,
                    walkingFlag,
                    z
                )
            ) {
                foundPath = true
                break
            }
            val thisCost = cost[curX][curY] + 1
            south@ do {
                if (curY > 0 && via[curX][curY - 1] == 0 && manager.getClippingFlag(
                        z,
                        absX,
                        absY - 1
                    ) and 0x12c010e == 0 && manager.getClippingFlag(
                        z,
                        absX + (size - 1),
                        absY - 1
                    ) and 0x12c0183 == 0
                ) {
                    for (i in 1 until size - 1) {
                        if (manager.getClippingFlag(z, absX + i, absY - 1) and 0x12c018f != 0) {
                            break@south
                        }
                    }
                    check(curX, curY - 1, SOUTH_FLAG, thisCost)
                }
            } while (false)
            west@ do {
                if (curX > 0 && via[curX - 1][curY] == 0 && manager.getClippingFlag(
                        z,
                        absX - 1,
                        absY
                    ) and 0x12c010e == 0 && manager.getClippingFlag(
                        z,
                        absX - 1,
                        absY + (size - 1)
                    ) and 0x12c0138 == 0
                ) {
                    for (i in 1 until size - 1) {
                        if (manager.getClippingFlag(z, absX - 1, absY + i) and 0x12c013e != 0) {
                            break@west
                        }
                    }
                    check(curX - 1, curY, WEST_FLAG, thisCost)
                }
            } while (false)
            north@ do {
                if (curY < 102 && via[curX][curY + 1] == 0 && manager.getClippingFlag(
                        z,
                        absX,
                        absY + size
                    ) and 0x12c0138 == 0 && manager.getClippingFlag(
                        z,
                        absX + (size - 1),
                        absY + size
                    ) and 0x12c01e0 == 0
                ) {
                    for (i in 1 until size - 1) {
                        if (manager.getClippingFlag(z, absX + i, absY + size) and 0x12c01f8 != 0) {
                            break@north
                        }
                    }
                    check(curX, curY + 1, NORTH_FLAG, thisCost)
                }
            } while (false)
            east@ do {
                if (curX < 102 && via[curX + 1][curY] == 0 && manager.getClippingFlag(
                        z,
                        absX + size,
                        absY
                    ) and 0x12c0183 == 0 && manager.getClippingFlag(
                        z,
                        absX + size,
                        absY + (size - 1)
                    ) and 0x12c01e0 == 0
                ) {
                    for (i in 1 until size - 1) {
                        if (manager.getClippingFlag(z, absX + size, absY + i) and 0x12c01e3 != 0) {
                            break@east
                        }
                    }
                    check(curX + 1, curY, EAST_FLAG, thisCost)
                }
            } while (false)
            southWest@ do {
                if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0 && manager.getClippingFlag(
                        z,
                        absX - 1,
                        absY + (size - 2)
                    ) and 0x12c0138 == 0 && manager.getClippingFlag(
                        z,
                        absX - 1,
                        absY - 1
                    ) and 0x12c010e == 0 && manager.getClippingFlag(
                        z,
                        absX + (size - 2),
                        absY - 1
                    ) and 0x12c0183 == 0
                ) {
                    for (i in 1 until size - 1) {
                        if (manager.getClippingFlag(
                                z,
                                absX - 1,
                                absY + (i - 1)
                            ) and 0x12c013e != 0 || manager.getClippingFlag(
                                z,
                                absX + (i - 1),
                                absY - 1
                            ) and 0x12c018f != 0
                        ) {
                            break@southWest
                        }
                    }
                    check(curX - 1, curY - 1, SOUTH_WEST_FLAG, thisCost)
                }
            } while (false)
            northWest@ do {
                if (curX > 0 && curY < 102 && via[curX - 1][curY + 1] == 0 && manager.getClippingFlag(
                        z,
                        absX - 1,
                        absY + 1
                    ) and 0x12c010e == 0 && manager.getClippingFlag(
                        z,
                        absX - 1,
                        absY + size
                    ) and 0x12c0138 == 0 && manager.getClippingFlag(z, absX, absY + size) and 0x12c01e0 == 0
                ) {
                    for (i in 1 until size - 1) {
                        if (manager.getClippingFlag(
                                z,
                                absX - 1,
                                absY + (i + 1)
                            ) and 0x12c013e != 0 || manager.getClippingFlag(
                                z,
                                absX + (i - 1),
                                absY + size
                            ) and 0x12c01f8 != 0
                        ) {
                            break@northWest
                        }
                    }
                    check(curX - 1, curY + 1, NORTH_WEST_FLAG, thisCost)
                }
            } while (false)
            southEast@ do {
                if (curX < 102 && curY > 0 && via[curX + 1][curY - 1] == 0 && manager.getClippingFlag(
                        z,
                        absX + 1,
                        absY - 1
                    ) and 0x12c010e == 0 && manager.getClippingFlag(
                        z,
                        absX + size,
                        absY - 1
                    ) and 0x12c0183 == 0 && manager.getClippingFlag(
                        z,
                        absX + size,
                        absY + (size - 2)
                    ) and 0x12c01e0 == 0
                ) {
                    for (i in 1 until size - 1) {
                        if (manager.getClippingFlag(
                                z,
                                absX + size,
                                absY + (i - 1)
                            ) and 0x12c01e3 != 0 || manager.getClippingFlag(
                                z,
                                absX + (i + 1),
                                absY - 1
                            ) and 0x12c018f != 0
                        ) {
                            break@southEast
                        }
                    }
                    check(curX + 1, curY - 1, SOUTH_EAST_FLAG, thisCost)
                }
            } while (false)
            if (curX < 102 && curY < 102 && via[curX + 1][curY + 1] == 0 && manager.getClippingFlag(
                    z,
                    absX + 1,
                    absY + size
                ) and 0x12c0138 == 0 && manager.getClippingFlag(
                    z,
                    absX + size,
                    absY + size
                ) and 0x12c01e0 == 0 && manager.getClippingFlag(z, absX + size, absY + 1) and 0x12c0183 == 0
            ) {
                for (i in 1 until size - 1) {
                    if (manager.getClippingFlag(
                            z,
                            absX + (i + 1),
                            absY + size
                        ) and 0x12c01f8 != 0 || manager.getClippingFlag(
                            z,
                            absX + size,
                            absY + (i + 1)
                        ) and 0x12c01e3 != 0
                    ) {
                        continue@main
                    }
                }
                check(curX + 1, curY + 1, NORTH_EAST_FLAG, thisCost)
            }
        }
    }

    private fun checkDoubleTraversal(
        end: Position,
        sizeX: Int,
        sizeY: Int,
        type: Int,
        rotation: Int,
        walkingFlag: Int,
        location: Position
    ) {
        var readPosition = 0
        val z: Int = location.z
        while (writePathPosition != readPosition) {
            curX = queueX[readPosition]
            curY = queueY[readPosition]
            readPosition = readPosition + 1 and 0xfff
            if (curX == dstX && curY == dstY) {
                foundPath = true
                break
            }
            val absX: Int = location.x + curX
            val absY: Int = location.y + curY
            if (type != 0) {
                if ((type < 5 || type == 10) && canDoorInteract(
                        absX,
                        absY,
                        2,
                        end.x,
                        end.y,
                        type - 1,
                        rotation,
                        z
                    )
                ) {
                    foundPath = true
                    break
                }
                if (type < 10 && canDecorationInteract(absX, absY, 2, end.x, end.y, type - 1, rotation, z)) {
                    foundPath = true
                    break
                }
            }
            if (sizeX != 0 && sizeY != 0 && canInteract(
                    absX,
                    absY,
                    2,
                    end.x,
                    end.y,
                    sizeX,
                    sizeY,
                    walkingFlag,
                    z
                )
            ) {
                foundPath = true
                break
            }
            val thisCost = cost[curX][curY] + 1
            if (curY > 0 && via[curX][curY - 1] == 0 && manager.getClippingFlag(
                    z,
                    absX,
                    absY - 1
                ) and 0x12c010e == 0 && manager.getClippingFlag(z, absX + 1, absY - 1) and 0x12c0183 == 0
            ) {
                check(curX, curY - 1, SOUTH_FLAG, thisCost)
            }
            if (curX > 0 && via[curX - 1][curY] == 0 && manager.getClippingFlag(
                    z,
                    absX - 1,
                    absY
                ) and 0x12c010e == 0 && manager.getClippingFlag(z, absX - 1, absY + 1) and 0x12c0138 == 0
            ) {
                check(curX - 1, curY, WEST_FLAG, thisCost)
            }
            if (curY < 102 && via[curX][curY + 1] == 0 && manager.getClippingFlag(
                    z,
                    absX,
                    absY + 2
                ) and 0x12c0138 == 0 && manager.getClippingFlag(z, absX + 1, absY + 2) and 0x12c01e0 == 0
            ) {
                check(curX, curY + 1, NORTH_FLAG, thisCost)
            }
            if (curX < 102 && via[curX + 1][curY] == 0 && manager.getClippingFlag(
                    z,
                    absX + 2,
                    absY
                ) and 0x12c0183 == 0 && manager.getClippingFlag(z, absX + 2, absY + 1) and 0x12c01e0 == 0
            ) {
                check(curX + 1, curY, EAST_FLAG, thisCost)
            }
            if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0 && manager.getClippingFlag(
                    z,
                    absX - 1,
                    absY - 1
                ) and 0x12c010e == 0 && manager.getClippingFlag(
                    z,
                    absX - 1,
                    absY
                ) and 0x12c0138 == 0 && manager.getClippingFlag(z, absX, absY - 1) and 0x12c0183 == 0
            ) {
                check(curX - 1, curY - 1, SOUTH_WEST_FLAG, thisCost)
            }
            if (curX > 0 && curY < 102 && via[curX - 1][curY + 1] == 0 && manager.getClippingFlag(
                    z,
                    absX - 1,
                    absY + 1
                ) and 0x12c010e == 0 && manager.getClippingFlag(
                    z,
                    absX - 1,
                    absY + 2
                ) and 0x12c0138 == 0 && manager.getClippingFlag(z, absX, absY + 2) and 0x12c01e0 == 0
            ) {
                check(curX - 1, curY + 1, NORTH_WEST_FLAG, thisCost)
            }
            if (curX < 102 && curY > 0 && via[curX + 1][curY - 1] == 0 && manager.getClippingFlag(
                    z,
                    absX + 1,
                    absY - 1
                ) and 0x12c010e == 0 && manager.getClippingFlag(
                    z,
                    absX + 2,
                    absY
                ) and 0x12c01e0 == 0 && manager.getClippingFlag(z, absX + 2, absY - 1) and 0x12c0183 == 0
            ) {
                check(curX + 1, curY - 1, SOUTH_EAST_FLAG, thisCost)
            }
            if (curX < 102 && curY < 102 && via[curX + 1][curY + 1] == 0 && manager.getClippingFlag(
                    z,
                    absX + 1,
                    absY + 2
                ) and 0x12c0138 == 0 && manager.getClippingFlag(
                    z,
                    absX + 2,
                    absY + 2
                ) and 0x12c01e0 == 0 && manager.getClippingFlag(z, absX + 2, absY + 1) and 0x12c0183 == 0
            ) {
                check(curX + 1, curY + 1, NORTH_EAST_FLAG, thisCost)
            }
        }
    }

    private fun checkSingleTraversal(
        end: Position,
        sizeX: Int,
        sizeY: Int,
        type: Int,
        rotation: Int,
        walkingFlag: Int,
        location: Position
    ) {
        var readPosition = 0
        val z: Int = location.z
        while (writePathPosition != readPosition) {
            curX = queueX[readPosition]
            curY = queueY[readPosition]
            readPosition = readPosition + 1 and 0xfff
            if (curX == dstX && curY == dstY) {
                foundPath = true
                break
            }
            val absX: Int = location.x + curX
            val absY: Int = location.y + curY
            if (type != 0) {
                if ((type < 5 || type == 10) && canDoorInteract(
                        absX,
                        absY,
                        1,
                        end.x,
                        end.y,
                        type - 1,
                        rotation,
                        z
                    )
                ) {
                    foundPath = true
                    break
                }
                if (type < 10 && canDecorationInteract(absX, absY, 1, end.x, end.y, type - 1, rotation, z)) {
                    foundPath = true
                    break
                }
            }
            if (sizeX != 0 && sizeY != 0 && canInteract(
                    absX,
                    absY,
                    1,
                    end.x,
                    end.y,
                    sizeX,
                    sizeY,
                    walkingFlag,
                    z
                )
            ) {
                foundPath = true
                break
            }
            val thisCost = cost[curX][curY] + 1
            if (curY > 0 && via[curX][curY - 1] == 0 && manager.getClippingFlag(
                    z,
                    absX,
                    absY - 1
                ) and 0x12c0102 == 0
            ) {
                check(curX, curY - 1, SOUTH_FLAG, thisCost)
            }
            if (curX > 0 && via[curX - 1][curY] == 0 && manager.getClippingFlag(
                    z,
                    absX - 1,
                    absY
                ) and 0x12c0108 == 0
            ) {
                check(curX - 1, curY, WEST_FLAG, thisCost)
            }
            if (curY < 103 && via[curX][curY + 1] == 0 && manager.getClippingFlag(
                    z,
                    absX,
                    absY + 1
                ) and 0x12c0120 == 0
            ) {
                check(curX, curY + 1, NORTH_FLAG, thisCost)
            }
            if (curX < 103 && via[curX + 1][curY] == 0 && manager.getClippingFlag(
                    z,
                    absX + 1,
                    absY
                ) and 0x12c0180 == 0
            ) {
                check(curX + 1, curY, EAST_FLAG, thisCost)
            }
            if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0 && manager.getClippingFlag(
                    z,
                    absX - 1,
                    absY - 1
                ) and 0x12c010e == 0 && manager.getClippingFlag(
                    z,
                    absX - 1,
                    absY
                ) and 0x12c0108 == 0 && manager.getClippingFlag(z, absX, absY - 1) and 0x12c0102 == 0
            ) {
                check(curX - 1, curY - 1, SOUTH_WEST_FLAG, thisCost)
            }
            if (curX > 0 && curY < 103 && via[curX - 1][curY + 1] == 0 && manager.getClippingFlag(
                    z,
                    absX - 1,
                    absY + 1
                ) and 0x12c0138 == 0 && manager.getClippingFlag(
                    z,
                    absX - 1,
                    absY
                ) and 0x12c0108 == 0 && manager.getClippingFlag(z, absX, absY + 1) and 0x12c0120 == 0
            ) {
                check(curX - 1, curY + 1, NORTH_WEST_FLAG, thisCost)
            }
            if (curX < 103 && curY > 0 && via[curX + 1][curY - 1] == 0 && manager.getClippingFlag(
                    z,
                    absX + 1,
                    absY - 1
                ) and 0x12c0183 == 0 && manager.getClippingFlag(
                    z,
                    absX + 1,
                    absY
                ) and 0x12c0180 == 0 && manager.getClippingFlag(z, absX, absY - 1) and 0x12c0102 == 0
            ) {
                check(curX + 1, curY - 1, SOUTH_EAST_FLAG, thisCost)
            }
            if (curX < 103 && curY < 103 && via[curX + 1][curY + 1] == 0 && manager.getClippingFlag(
                    z,
                    absX + 1,
                    absY + 1
                ) and 0x12c01e0 == 0 && manager.getClippingFlag(
                    z,
                    absX + 1,
                    absY
                ) and 0x12c0180 == 0 && manager.getClippingFlag(z, absX, absY + 1) and 0x12c0120 == 0
            ) {
                check(curX + 1, curY + 1, NORTH_EAST_FLAG, thisCost)
            }
        }
    }

    override fun isStandingIn(
        x: Int,
        y: Int,
        moverSizeX: Int,
        moverSizeY: Int,
        destX: Int,
        destY: Int,
        sizeX: Int,
        sizeY: Int
    ): Boolean {
        return if (x >= sizeX + destX || moverSizeX + x <= destX) {
            false
        } else destY + sizeY > y && y + moverSizeY > destY
    }

    override fun canInteract(
        x: Int,
        y: Int,
        moverSize: Int,
        destX: Int,
        destY: Int,
        sizeX: Int,
        sizeY: Int,
        walkFlag: Int,
        z: Int
    ): Boolean {
        if (moverSize > 1) {
            return if (isStandingIn(
                    x,
                    y,
                    moverSize,
                    moverSize,
                    destX,
                    destY,
                    sizeX,
                    sizeY
                )
            ) {
                true
            } else canInteractSized(
                x,
                y,
                moverSize,
                moverSize,
                destX,
                destY,
                sizeX,
                sizeY,
                walkFlag,
                z
            )
        }
        val flag: Int = manager.getClippingFlag(z, x, y)
        val cornerX = destX + sizeX - 1
        val cornerY = destY + sizeY - 1
        if (x in destX..cornerX && y >= destY && y <= cornerY) {
            return true
        }
        if (x == destX - 1 && destY <= y && y <= cornerY && 0x8 and flag == 0 && 0x8 and walkFlag == 0) {
            return true
        }
        if (x == cornerX + 1 && destY <= y && cornerY >= y && flag and 0x80 == 0 && 0x2 and walkFlag == 0) {
            return true
        }
        return if (y == destY - 1 && destX <= x && cornerX >= x && 0x2 and flag == 0 && 0x4 and walkFlag == 0) {
            true
        } else y == cornerY + 1 && destX <= x && cornerX >= x && flag and 0x20 == 0 && 0x1 and walkFlag == 0
    }

    override fun canInteractSized(
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
    ): Boolean {
        val fromCornerY = curY + moverSizeY
        val fromCornerX = curX + moverSizeX
        val toCornerX = sizeX + destX
        val toCornerY = sizeY + destY
        if (curX in destX until toCornerX) {
            if (destY == fromCornerY && walkingFlag and 0x4 == 0) {
                var x = curX
                val endX = if (toCornerX < fromCornerX) toCornerX else fromCornerX
                while (endX > x) {
                    if (manager.getClippingFlag(z, x, -1 + fromCornerY) and 0x2 == 0) {
                        return true
                    }
                    x++
                }
            } else if (toCornerY == curY && walkingFlag and 0x1 == 0) {
                var x = curX
                val endX = if (fromCornerX <= toCornerX) fromCornerX else toCornerX
                while (x < endX) {
                    if (manager.getClippingFlag(z, x, curY) and 0x20 == 0) {
                        return true
                    }
                    x++
                }
            }
        } else if (fromCornerX in (destX + 1)..toCornerX) {
            if (fromCornerY == destY && 0x4 and walkingFlag == 0) {
                var x = destX
                while (fromCornerX > x) {
                    if (manager.getClippingFlag(z, x, -1 + fromCornerY) and 0x2 == 0) {
                        return true
                    }
                    x++
                }
            } else if (toCornerY == curY && 0x1 and walkingFlag == 0) {
                var x = destX
                while (fromCornerX > x) {
                    if (manager.getClippingFlag(z, x, curY) and 0x20 == 0) {
                        return true
                    }
                    x++
                }
            }
        } else if (curY < destY || curY >= toCornerY) {
            if (fromCornerY in (destY + 1)..toCornerY) {
                if (fromCornerX == destX && walkingFlag and 0x8 == 0) {
                    for (y in destY until fromCornerY) {
                        if (manager.getClippingFlag(z, -1 + fromCornerX, y) and 0x8 == 0) {
                            return true
                        }
                    }
                } else if (curX == toCornerX && 0x2 and walkingFlag == 0) {
                    var y = destY
                    while (fromCornerY > y) {
                        if (manager.getClippingFlag(z, curX, y) and 0x80 == 0) {
                            return true
                        }
                        y++
                    }
                }
            }
        } else if (destX != fromCornerX || 0x8 and walkingFlag != 0) {
            if (curX == toCornerX && walkingFlag and 0x2 == 0) {
                var y = curY
                val endY = if (fromCornerY <= toCornerY) fromCornerY else toCornerY
                while (y < endY) {
                    if (0x80 and manager.getClippingFlag(z, curX, y) == 0) {
                        return true
                    }
                    y++
                }
            }
        } else {
            var y = curY
            val endY = if (fromCornerY > toCornerY) toCornerY else fromCornerY
            while (endY > y) {
                if (manager.getClippingFlag(z, fromCornerX - 1, y) and 0x8 == 0) {
                    return true
                }
                y++
            }
        }
        return false
    }

    override fun canDecorationInteract(
        curX: Int,
        curY: Int,
        size: Int,
        destX: Int,
        destY: Int,
        rotation: Int,
        type: Int,
        z: Int
    ): Boolean {
        var rot = rotation
        if (size != 1) {
            if (destX >= curX && destX <= curX + size - 1 && destY >= destY && destY <= destY + size - 1) {
                return true
            }
        } else if (destX == curX && curY == destY) {
            return true
        }
        if (size == 1) {
            val flag: Int = manager.getClippingFlag(z, curX, curY)
            if (type == 6 || type == 7) {
                if (type == 7) {
                    rot = rot + 2 and 0x3
                }
                if (rot == 0) {
                    if (curX == 1 + destX && curY == destY && 0x80 and flag == 0) {
                        return true
                    }
                    if (destX == curX && curY == destY - 1 && flag and 0x2 == 0) {
                        return true
                    }
                } else if (rot == 1) {
                    if (curX == destX - 1 && curY == destY && 0x8 and flag == 0) {
                        return true
                    }
                    if (curX == destX && curY == destY - 1 && flag and 0x2 == 0) {
                        return true
                    }
                } else if (rot == 2) {
                    if (destX - 1 == curX && destY == curY && flag and 0x8 == 0) {
                        return true
                    }
                    if (destX == curX && destY + 1 == curY && 0x20 and flag == 0) {
                        return true
                    }
                } else if (rot == 3) {
                    if (destX + 1 == curX && curY == destY && 0x80 and flag == 0) {
                        return true
                    }
                    if (destX == curX && curY == destY + 1 && 0x20 and flag == 0) {
                        return true
                    }
                }
            }
            if (type == 8) {
                if (destX == curX && curY == destY + 1 && flag and 0x20 == 0) {
                    return true
                }
                if (destX == curX && -1 + destY == curY && 0x2 and flag == 0) {
                    return true
                }
                return if (curX == destX - 1 && curY == destY && 0x8 and flag == 0) {
                    true
                } else curX == destX + 1 && curY == destY && flag and 0x80 == 0
            }
        } else {
            val cornerX = curX + size - 1
            val cornerY = curY + size - 1
            if (type == 6 || type == 7) {
                if (type == 7) {
                    rot = 0x3 and 2 + rot
                }
                if (rot == 0) {
                    if (destX + 1 == curX && destY >= curY && destY <= cornerY && manager.getClippingFlag(
                            z,
                            curX,
                            destY
                        ) and 0x80 == 0
                    ) {
                        return true
                    }
                    if (destX in curX..cornerX && destY - size == curY && 0x2 and manager.getClippingFlag(
                            z,
                            destX,
                            cornerY
                        ) == 0
                    ) {
                        return true
                    }
                } else if (rot == 1) {
                    if (-size + destX == curX && destY >= curY && cornerY >= destY && manager.getClippingFlag(
                            z,
                            cornerX,
                            destY
                        ) and 0x8 == 0
                    ) {
                        return true
                    }
                    if (destX in curX..cornerX && -size + destY == curY && manager.getClippingFlag(
                            z,
                            destX,
                            cornerY
                        ) and 0x2 == 0
                    ) {
                        return true
                    }
                } else if (rot == 2) {
                    if (curX == destX - size && curY <= destY && destY <= cornerY && 0x8 and manager.getClippingFlag(
                            z,
                            cornerX,
                            destY
                        ) == 0
                    ) {
                        return true
                    }
                    if (destX in curX..cornerX && destY + 1 == curY && 0x20 and manager.getClippingFlag(
                            z,
                            destX,
                            curY
                        ) == 0
                    ) {
                        return true
                    }
                } else if (rot == 3) {
                    if (1 + destX == curX && curY <= destY && destY <= cornerY && 0x80 and manager.getClippingFlag(
                            z,
                            curX,
                            destY
                        ) == 0
                    ) {
                        return true
                    }
                    if (destX in curX..cornerX && 1 + destY == curY && manager.getClippingFlag(
                            z,
                            destX,
                            curY
                        ) and 0x20 == 0
                    ) {
                        return true
                    }
                }
            }
            if (type == 8) {
                if (destX in curX..cornerX && 1 + destY == curY && manager.getClippingFlag(
                        z,
                        destX,
                        curY
                    ) and 0x20 == 0
                ) {
                    return true
                }
                if (destX in curX..cornerX && curY == -size + destY && 0x2 and manager.getClippingFlag(
                        z,
                        destX,
                        cornerY
                    ) == 0
                ) {
                    return true
                }
                return if (curX == -size + destX && destY >= curY && destY <= cornerY && 0x8 and manager.getClippingFlag(
                        z,
                        cornerX,
                        destY
                    ) == 0
                ) {
                    true
                } else 1 + destX == curX && curY <= destY && cornerY >= destY && manager.getClippingFlag(
                    z,
                    curX,
                    destY
                ) and 0x80 == 0
            }
        }
        return false
    }


    override fun canDoorInteract(
        curX: Int,
        curY: Int,
        size: Int,
        destX: Int,
        destY: Int,
        type: Int,
        rotation: Int,
        z: Int
    ): Boolean {
        if (size != 1) {
            if (destX >= curX && destX <= size + curX - 1 && destY >= destY && destY <= destY + size - 1) {
                return true
            }
        } else if (curX == destX && destY == curY) {
            return true
        }
        if (size == 1) {
            if (type == 0) {
                when (rotation) {
                    0 -> {
                        if (curX == destX - 1 && destY == curY) {
                            return true
                        }
                        return if (destX == curX && 1 + destY == curY && 0x12c0120 and manager.getClippingFlag(
                                z,
                                curX,
                                curY
                            ) == 0
                        ) {
                            true
                        } else curX == destX && destY - 1 == curY && manager.getClippingFlag(
                            z,
                            curX,
                            curY
                        ) and 0x12c0102 == 0
                    }
                    1 -> {
                        if (curX == destX && destY + 1 == curY) {
                            return true
                        }
                        return if (curX == destX - 1 && curY == destY && 0x12c0108 and manager.getClippingFlag(
                                z,
                                curX,
                                curY
                            ) == 0
                        ) {
                            true
                        } else curX == 1 + destX && destY == curY && 0x12c0180 and manager.getClippingFlag(
                            z,
                            curX,
                            curY
                        ) == 0
                    }
                    2 -> {
                        if (1 + destX == curX && destY == curY) {
                            return true
                        }
                        return if (destX == curX && 1 + destY == curY && 0x12c0120 and manager.getClippingFlag(
                                z,
                                curX,
                                curY
                            ) == 0
                        ) {
                            true
                        } else curX == destX && curY == destY - 1 && manager.getClippingFlag(
                            z,
                            curX,
                            curY
                        ) and 0x12c0102 == 0
                    }
                    3 -> {
                        if (curX == destX && -1 + destY == curY) {
                            return true
                        }
                        return if (curX == -1 + destX && destY == curY && 0x12c0108 and manager.getClippingFlag(
                                z,
                                curX,
                                curY
                            ) == 0
                        ) {
                            true
                        } else curX == 1 + destX && destY == curY && manager.getClippingFlag(
                            z,
                            curX,
                            curY
                        ) and 0x12c0180 == 0
                    }
                }
            } else if (type == 2) {
                when (rotation) {
                    0 -> {
                        if (destX - 1 == curX && curY == destY) {
                            return true
                        }
                        if (destX == curX && curY == 1 + destY) {
                            return true
                        }
                        return if (curX == destX + 1 && curY == destY && 0x12c0180 and manager.getClippingFlag(
                                z,
                                curX,
                                curY
                            ) == 0
                        ) {
                            true
                        } else curX == destX && destY - 1 == curY && manager.getClippingFlag(
                            z,
                            curX,
                            curY
                        ) and 0x12c0102 == 0
                    }
                    1 -> {
                        if (curX == destX - 1 && curY == destY && 0x12c0108 and manager.getClippingFlag(
                                z,
                                curX,
                                curY
                            ) == 0
                        ) {
                            return true
                        }
                        if (curX == destX && curY == 1 + destY) {
                            return true
                        }
                        return if (1 + destX == curX && curY == destY) {
                            true
                        } else curX == destX && destY - 1 == curY && manager.getClippingFlag(
                            z,
                            curX,
                            curY
                        ) and 0x12c0102 == 0
                    }
                    2 -> {
                        if (destX - 1 == curX && destY == curY && 0x12c0108 and manager.getClippingFlag(
                                z,
                                curX,
                                curY
                            ) == 0
                        ) {
                            return true
                        }
                        if (destX == curX && 1 + destY == curY && 0x12c0120 and manager.getClippingFlag(
                                z,
                                curX,
                                curY
                            ) == 0
                        ) {
                            return true
                        }
                        return if (1 + destX == curX && curY == destY) {
                            true
                        } else curX == destX && curY == destY - 1
                    }
                    3 -> {
                        if (destX - 1 == curX && curY == destY) {
                            return true
                        }
                        if (destX == curX && curY == destY + 1 && 0x12c0120 and manager.getClippingFlag(
                                z,
                                curX,
                                curY
                            ) == 0
                        ) {
                            return true
                        }
                        return if (curX == 1 + destX && curY == destY && manager.getClippingFlag(
                                z,
                                curX,
                                curY
                            ) and 0x12c0180 == 0
                        ) {
                            true
                        } else destX == curX && destY - 1 == curY
                    }
                }
            } else if (type == 9) {
                if (curX == destX && curY == destY + 1 && manager.getClippingFlag(z, curX, curY) and 0x20 == 0) {
                    return true
                }
                if (curX == destX && curY == destY - 1 && manager.getClippingFlag(z, curX, curY) and 0x2 == 0) {
                    return true
                }
                return if (curX == destX - 1 && curY == destY && 0x8 and manager.getClippingFlag(
                        z,
                        curX,
                        curY
                    ) == 0
                ) {
                    true
                } else destX + 1 == curX && curY == destY && 0x80 and manager.getClippingFlag(z, curX, curY) == 0
            }
        } else {
            val cornerX = curX - (1 - size)
            val cornerY = -1 + curY + size
            if (type == 0) {
                if (rotation == 0) {
                    if (destX - size == curX && destY >= curY && destY <= cornerY) {
                        return true
                    }
                    if (destX in curX..cornerX && curY == 1 + destY && manager.getClippingFlag(
                            z,
                            destX,
                            curY
                        ) and 0x12c0120 == 0
                    ) {
                        return true
                    }
                    if (destX in curX..cornerX && destY - size == curY && manager.getClippingFlag(
                            z,
                            destX,
                            cornerY
                        ) and 0x12c0102 == 0
                    ) {
                        return true
                    }
                } else if (rotation == 1) {
                    if (destX in curX..cornerX && destY + 1 == curY) {
                        return true
                    }
                    if (curX == -size + destX && destY >= curY && cornerY >= destY && 0x12c0108 and manager.getClippingFlag(
                            z,
                            cornerX,
                            destY
                        ) == 0
                    ) {
                        return true
                    }
                    if (curX == 1 + destX && destY >= curY && cornerY >= destY && manager.getClippingFlag(
                            z,
                            curX,
                            destY
                        ) and 0x12c0180 == 0
                    ) {
                        return true
                    }
                } else if (rotation == 2) {
                    if (curX == 1 + destX && curY <= destY && destY <= cornerY) {
                        return true
                    }
                    if (destX in curX..cornerX && destY + 1 == curY && 0x12c0120 and manager.getClippingFlag(
                            z,
                            destX,
                            curY
                        ) == 0
                    ) {
                        return true
                    }
                    if (destX in curX..cornerX && destY - size == curY && 0x12c0102 and manager.getClippingFlag(
                            z,
                            destX,
                            cornerY
                        ) == 0
                    ) {
                        return true
                    }
                } else if (rotation == 3) {
                    if (destX in curX..cornerX && curY == -size + destY) {
                        return true
                    }
                    if (-size + destX == curX && curY <= destY && destY <= cornerY && manager.getClippingFlag(
                            z,
                            cornerX,
                            destY
                        ) and 0x12c0108 == 0
                    ) {
                        return true
                    }
                    if (1 + destX == curX && curY <= destY && cornerY >= destY && manager.getClippingFlag(
                            z,
                            curX,
                            destY
                        ) and 0x12c0180 == 0
                    ) {
                        return true
                    }
                }
            }
            if (type == 2) {
                if (rotation == 0) {
                    if (destX - size == curX && curY <= destY && destY <= cornerY) {
                        return true
                    }
                    if (destX in curX..cornerX && curY == 1 + destY) {
                        return true
                    }
                    if (curX == 1 + destX && curY <= destY && destY <= cornerY && 0x12c0180 and manager.getClippingFlag(
                            z,
                            curX,
                            destY
                        ) == 0
                    ) {
                        return true
                    }
                    if (destX in curX..cornerX && -size + destY == curY && manager.getClippingFlag(
                            z,
                            destX,
                            cornerY
                        ) and 0x12c0102 == 0
                    ) {
                        return true
                    }
                } else if (rotation == 1) {
                    if (-size + destX == curX && destY >= curY && destY <= cornerY && manager.getClippingFlag(
                            z,
                            cornerX,
                            destY
                        ) and 0x12c0108 == 0
                    ) {
                        return true
                    }
                    if (destX in curX..cornerX && curY == 1 + destY) {
                        return true
                    }
                    if (destX + 1 == curX && curY <= destY && destY <= cornerY) {
                        return true
                    }
                    if (destX in curX..cornerX && destY + -size == curY && 0x12c0102 and manager.getClippingFlag(
                            z,
                            destX,
                            cornerY
                        ) == 0
                    ) {
                        return true
                    }
                } else if (rotation == 2) {
                    if (curX == destX - size && curY <= destY && cornerY >= destY && manager.getClippingFlag(
                            z,
                            cornerX,
                            destY
                        ) and 0x12c0108 == 0
                    ) {
                        return true
                    }
                    if (destX in curX..cornerX && 1 + destY == curY && 0x12c0120 and manager.getClippingFlag(
                            z,
                            destX,
                            curY
                        ) == 0
                    ) {
                        return true
                    }
                    if (1 + destX == curX && destY >= curY && cornerY >= destY) {
                        return true
                    }
                    if (destX in curX..cornerX && curY == -size + destY) {
                        return true
                    }
                } else if (rotation == 3) {
                    if (destX + -size == curX && destY >= curY && destY <= cornerY) {
                        return true
                    }
                    if (destX in curX..cornerX && curY == 1 + destY && manager.getClippingFlag(
                            z,
                            destX,
                            curY
                        ) and 0x12c0120 == 0
                    ) {
                        return true
                    }
                    if (1 + destX == curX && destY >= curY && cornerY >= destY && 0x12c0180 and manager.getClippingFlag(
                            z,
                            curX,
                            destY
                        ) == 0
                    ) {
                        return true
                    }
                    if (destX in curX..cornerX && curY == -size + destY) {
                        return true
                    }
                }
            }
            if (type == 9) {
                if (destX in curX..cornerX && curY == 1 + destY && manager.getClippingFlag(
                        z,
                        destX,
                        curY
                    ) and 0x12c0120 == 0
                ) {
                    return true
                }
                if (destX in curX..cornerX && curY == -size + destY && 0x12c0102 and manager.getClippingFlag(
                        z,
                        destX,
                        cornerY
                    ) == 0
                ) {
                    return true
                }
                return if (-size + destX == curX && destY >= curY && cornerY >= destY && 0x12c0108 and manager.getClippingFlag(
                        z,
                        cornerX,
                        destY
                    ) == 0
                ) {
                    true
                } else curX == destX + 1 && destY >= curY && cornerY >= destY && manager.getClippingFlag(
                    z,
                    curX,
                    destY
                ) and 0x12c0180 == 0
            }
        }
        return false
    }


    override fun check(x: Int, y: Int, direction: Int, currentCost: Int) {
        queueX[writePathPosition] = x
        queueY[writePathPosition] = y
        via[x][y] = direction
        cost[x][y] = currentCost
        writePathPosition = writePathPosition + 1 and 0xfff
    }

    override fun reset() {
        queueX.fill(0)
        queueY.fill(0)
        via.forEach { it.fill(0) }
        cost.forEach { it.fill(0) }
        writePathPosition = 0
    }


}