package com.arandarkt.game.api.components.entity.player

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.components.entity.flags.player.FaceEntityFlag
import com.arandarkt.game.api.components.entity.flags.player.FacePositionFlag
import com.arandarkt.game.api.entity.Entity
import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.entity.character.Character.Companion.isPlayer
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.entity.hasComponent
import com.arandarkt.game.api.koin.inject
import com.arandarkt.game.api.packets.GameSession
import com.arandarkt.game.api.world.location.components.Position
import com.arandarkt.game.api.world.map.Direction
import com.arandarkt.game.api.world.map.GameRegionManager
import com.arandarkt.game.api.world.map.Point
import io.guthix.buffer.BitBuf
import java.util.*
import kotlin.math.abs

class MovementComponent(val entity: Character) : Component {

    private val regionManager: GameRegionManager by inject()

    var walkingDirection: Int = -1
    var runningDirection: Int = -1

    var direction: Direction = Direction.SOUTH

    var isLocked: Boolean = false
    var isFrozen: Boolean = false
    var isRunning: Boolean = false
    var isRunDisabled: Boolean = false

    var isTeleporting: Boolean = false

    var teleportPosition: Position = Position.VOID_LOCATION
    var footPrint: Position = Position.VOID_LOCATION

    val walkingQueue: Deque<Point> = LinkedList()

    var runEnergy: Double = 1.0

    val isMoving: Boolean get() = walkingDirection != -1 || runningDirection != -1

    fun face(entity: Entity) {
        if (entity.hasComponent<FaceEntityOrPositionComponent>()) {
            val masks = this.entity.component<UpdateMasksComponent>()
            val comp = entity.component<FaceEntityOrPositionComponent>()
            if (comp.shouldFaceEntity()) {
                masks.with(FaceEntityFlag(comp.entityIndex))
            } else {
                masks.with(FaceEntityFlag(-1))
            }
        } else if(entity.hasComponent<Position>()) {
            face(entity.component<Position>())
        }
    }

    fun face(pos: Position) {
        val masks = this.entity.component<UpdateMasksComponent>()
        masks.with(FacePositionFlag(pos))
    }

    fun clearEntityFacingDirection() {
        entity.component<UpdateMasksComponent>().with(FaceEntityFlag(-1))
    }

    fun clearEntityPositionDirection() {
        entity.component<UpdateMasksComponent>().with(FacePositionFlag(Position(0, 0)))
    }

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

    fun updateTeleport(): Boolean {
        if (teleportPosition !== Position.VOID_LOCATION) {
            reset(false)
            entity.component<Position>().update(teleportPosition)
            teleportPosition = Position.VOID_LOCATION
            if (entity.hasComponent<UpdateMasksComponent>()) {
                val masks = entity.component<UpdateMasksComponent>()
                var last = masks.lastSceneGraph
                val myPos = entity.component<Position>()
                if (last === Position.VOID_LOCATION) {
                    last = entity.component()
                }
                if (last.getRegionX() - myPos.getRegionX() >= 4 || last.getRegionX() - myPos.getRegionX() <= -4) {
                    masks.shouldUpdateSceneGraph = true
                } else if (last.getRegionY() - myPos.getRegionY() >= 4 || last.getRegionY() - myPos.getRegionY() <= -4) {
                    masks.shouldUpdateSceneGraph = true
                }
            }
            regionManager.movePlayer(entity)
            footPrint = entity.component()
            isTeleporting = true
            return true
        }
        return false
    }

    fun updateRegion(location: Position, move: Boolean): Boolean {
        val masks = entity.component<UpdateMasksComponent>()
        var lastRegion = masks.lastSceneGraph
        if (lastRegion == Position.VOID_LOCATION) {
            lastRegion = location
        }
        val rx: Int = lastRegion.getRegionX()
        val ry: Int = lastRegion.getRegionY()
        val cx: Int = location.getRegionX()
        val cy: Int = location.getRegionY()
        if (rx - cx >= 4) {
            masks.shouldUpdateSceneGraph = true
        } else if (rx - cx <= -4) {
            masks.shouldUpdateSceneGraph = true
        }
        if (ry - cy >= 4) {
            masks.shouldUpdateSceneGraph = true
        } else if (ry - cy <= -4) {
            masks.shouldUpdateSceneGraph = true
        }
        if (move && masks.shouldUpdateSceneGraph) {
            regionManager.movePlayer(entity)
            return true
        }
        return false
    }

    fun updateMovement() {
        val isPlayer = entity.isPlayer
        this.walkingDirection = -1
        this.runningDirection = -1
        if (updateTeleport()) {
            return
        }
        if (isPlayer && updateRegion(entity.component(), true)) {
            return
        }
        var point = walkingQueue.poll()
        if (point == null) {
            //updateRunEnergy(false)
            return
        }
        if (isPlayer && runEnergy < 1.0) {
            isRunning = false
        }
        var runPoint: Point? = null
        if (point.direction == null) {
            point = walkingQueue.poll()
        }
        var walkDirection = -1
        var runDirection = -1
        if (isRunning && (point == null || !point.isRunDisabled)) {
            runPoint = walkingQueue.poll()
        }
        if (point != null) {
            if (point.direction == null) {
                return
            }
            walkDirection = point.direction!!.ordinal
        }
        if (runPoint != null) {
            runDirection = runPoint.direction?.ordinal ?: -1
        }
        var diffX = 0
        var diffY = 0
        if (walkDirection != -1) {
            diffX = point.diffX
            diffY = point.diffY
        }
        if (runDirection != -1) {
            footPrint = entity.component<Position>().transform(diffX, diffY, 0)
            diffX += runPoint?.diffX ?: 0
            diffY += runPoint?.diffY ?: 0
            if(entity.isPlayer) {
                val session = entity.component<GameSession>()
                //session.sendPacket(RunEnergy())
            }
        }
        if (diffX != 0 || diffY != 0) {
            var walk = entity.component<Position>()
            if (point != null) {
                walk = walk.transform(point.diffX, point.diffY, 0)
                /*if (!entity.getZoneMonitor().move(entity.getLocation(), walk)) {
                    reset()
                    if (entity.getPulseManager().isMovingPulse()) {
                        entity.getPulseManager().clear() // TODO: Check for bugs
                    }
                    return
                }*/
            }
            val dest = entity.component<Position>().transform(diffX, diffY, 0)
            if (runPoint != null) {
                /*if (!entity.getZoneMonitor().move(walk, dest)) {
                    dest = dest.transform(-runPoint.diffX, -runPoint.diffY, 0)
                    runPoint = null
                    runDirection = -1
                    reset()
                    if (entity.getPulseManager().isMovingPulse()) {
                        entity.getPulseManager().clear() // TODO: Check for
                        // bugs
                    }
                }*/
            }
            if (runPoint?.direction != null) {
                direction = runPoint.direction!!
            } else if (point?.direction != null) {
                direction = point.direction!!
            }
            footPrint = entity.component()
            entity.component<Position>().update(dest)
            regionManager.movePlayer(entity)
        }
        this.walkingDirection = walkDirection
        this.runningDirection = runDirection
    }

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