package com.arandarkt.game.api.world.map.path

import com.arandarkt.game.api.koin.inject
import com.arandarkt.game.api.world.location.components.Position
import com.arandarkt.game.api.world.map.GameRegionManager
import org.rsmod.pathfinder.Route
import org.rsmod.pathfinder.SmartPathFinder

class PathFinder {

    private val regionManager: GameRegionManager by inject()

    fun smartRoute(srcPos: Position, destPos: Position) : Route {
        return smartRoute(srcPos.x, srcPos.y, destPos.x, destPos.y, srcPos.z)
    }

    fun smartRoute(srcX: Int, srcY: Int, destX: Int, destY: Int, level: Int): Route {
        val pf = SmartPathFinder()
        val flags = clipFlags(srcX, srcY, level, pf.searchMapSize)
        return pf.findPath(flags, srcX, srcY, destX, destY)
    }

    fun clipFlags(centerX: Int, centerY: Int, level: Int, size: Int): IntArray {
        val half = size / 2
        val flags = IntArray(size * size)
        val rangeX = centerX - half until centerX + half
        val rangeY = centerY - half until centerY + half
        for (y in rangeY) {
            for (x in rangeX) {
                val flag = regionManager.getClippingFlag(Position(x, y, level))
                val lx = x - (centerX - half)
                val ly = y - (centerY - half)
                val index = (ly * size) + lx
                flags[index] = flag
            }
        }
        return flags
    }
}