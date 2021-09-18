package com.arandarkt.game.api.components.entity.player

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.world.location.components.Position
import com.arandarkt.game.api.world.map.Direction
import com.arandarkt.game.api.world.map.Point
import io.guthix.buffer.BitBuf
import java.util.*
import kotlin.math.abs

class MovementComponent(val entity: Character) : Component {

    var walkingDirection: Int = -1
    var runningDirection: Int = -1

    var direction: Direction = Direction.SOUTH

    var isLocked: Boolean = false
    var isFrozen: Boolean = false
    var isRunning: Boolean = false
    var isRunDisabled: Boolean = false

    val isTeleporting: Boolean
        get() = teleportPosition !== Position.VOID_LOCATION

    var teleportPosition: Position = Position.VOID_LOCATION
    var footPrint: Position = Position.VOID_LOCATION

    val walkingQueue: Deque<Point> = LinkedList()

    fun addPath(x: Int, y: Int, runDisabled: Boolean = isRunDisabled) {
        val point = walkingQueue.peekLast() ?: return
        var diffX: Int = x - point.x
        var diffY: Int = y - point.y
        val max = abs(diffX).coerceAtLeast(abs(diffY))
        for (i in 0 until max) {
            if (diffX < 0) {
                diffX++
            } else if (diffX > 0) {
                diffX--
            }
            if (diffY < 0) {
                diffY++
            } else if (diffY > 0) {
                diffY--
            }
            addPoint(x - diffX, y - diffY, runDisabled)
        }
    }

    fun addPoint(x: Int, y: Int, runDisabled: Boolean) {
        val point = walkingQueue.peekLast() ?: return
        val diffX: Int = x - point.x
        val diffY: Int = y - point.y
        val direction = Direction.getDirection(diffX, diffY)
        walkingQueue.add(Point(x, y, direction, diffX, diffY, runDisabled))
    }

    fun reset(running: Boolean = isRunning) {
        val pos = entity.component<Position>()
        walkingQueue.clear()
        walkingQueue.add(Point(pos.x, pos.y))
        this.isRunning = running
    }

    fun walkBack() {
        reset()
        addPath(footPrint.x, footPrint.y)
    }

    override suspend fun onTick(currentTick: Long) {}

    override fun BitBuf.save() {
        writeBoolean(isRunning)
        writeBoolean(isFrozen)
        writeBoolean(isLocked)
        writeBoolean(isRunDisabled)
        with(footPrint) { save() }
    }

    override fun BitBuf.load() {
        isRunning = readBoolean()
        isFrozen = readBoolean()
        isLocked = readBoolean()
        isRunDisabled = readBoolean()
        with(footPrint) { load() }
    }
}