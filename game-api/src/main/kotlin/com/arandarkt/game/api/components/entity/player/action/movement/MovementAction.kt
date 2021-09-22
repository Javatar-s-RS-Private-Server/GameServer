package com.arandarkt.game.api.components.entity.player.action.movement

import com.arandarkt.game.api.components.ComponentManager
import com.arandarkt.game.api.components.entity.player.FaceEntityOrPositionComponent
import com.arandarkt.game.api.components.entity.player.MovementComponent
import com.arandarkt.game.api.entity.Entity
import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.entity.character.action.ActionComponent
import com.arandarkt.game.api.entity.character.action.CharacterAction
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.entity.hasComponent
import com.arandarkt.game.api.koin.inject
import com.arandarkt.game.api.packets.GameSession
import com.arandarkt.game.api.packets.GameSession.Companion.sendPacket
import com.arandarkt.game.api.packets.outgoing.ClearMinimapFlag
import com.arandarkt.game.api.packets.outgoing.GameMessage
import com.arandarkt.game.api.world.location.components.Position
import com.arandarkt.game.api.world.location.components.Position.Companion.VOID_LOCATION
import com.arandarkt.game.api.world.location.components.Position.Companion.getDelta
import com.arandarkt.game.api.world.map.Direction
import com.arandarkt.game.api.world.map.path.PathFinder
import org.rsmod.pathfinder.reach.ReachStrategy

class MovementAction(
    val dest: Entity,
    val forceRun: Boolean = false,
    var lastPos: Position = VOID_LOCATION
) : CharacterAction {
    override val components = ComponentManager<ActionComponent>()
    override var isActive: Boolean = false
    override var isCanceled: Boolean = false
    override var isCompleted: Boolean = false

    private var interactLocation: Position = VOID_LOCATION
    private var near = false
    private val pathFinder: PathFinder by inject()

    override fun onCancel(entity: Character) {
        if(entity.hasComponent<MovementComponent>()) {
            entity.component<MovementComponent>().reset()
        }
    }

    override suspend fun fireAction(entity: Character): Boolean {
        val myPos = entity.component<Position>()
        if(!isActive) {
            return true
        }
        findPath(entity)
        if (myPos == interactLocation) {
            if (near || onAction(entity)) {
                if (entity.hasComponent<GameSession>()) {
                    val session = entity.component<GameSession>()
                    if (near) {
                        session.sendPacket(GameMessage("I can't reach that."))
                    }
                    session.sendPacket(ClearMinimapFlag())
                }
                isCompleted = true
                onCompleted(entity)
                return true
            }
        }
        return false
    }

    override suspend fun onAction(entity: Character): Boolean {
        return true
    }

    private fun findPath(entity: Character) {
        if(!dest.hasComponent<Position>() || !entity.hasComponent<Position>())
            return
        val myPos = entity.component<Position>()
        val myMov = entity.component<MovementComponent>()
        val isInsideEntity = isInsideEntity(entity)
        if(lastPos !== VOID_LOCATION && lastPos == entity.component<Position>() && !isInsideEntity) {
            return
        }
        val path = pathFinder.smartRoute(entity.component(), dest.component())
        near = !path.success || path.alternative

        println("Success? ${path.success} or ${path.alternative}")
        interactLocation = myPos
        println("Points ${path.coords.size} - $interactLocation")
        if (path.coords.isNotEmpty()) {
            val point = path.coords.last()
            println("Point $point")
            interactLocation = Position(point.x, point.y, myPos.z)
            if (forceRun) {
                myMov.reset(forceRun)
            } else {
                myMov.reset()
            }
            path.coords.forEach {
                myMov.addPath(point.x, point.y)
            }
        }
        lastPos = dest.component()
        if (dest.hasComponent<FaceEntityOrPositionComponent>()) {
            myMov.face(dest)
        } else {
            myMov.clearEntityFacingDirection()
        }
    }

    private fun isInsideEntity(player: Character): Boolean {
        if(!dest.hasComponent<Position>())
            return false
        val l = player.component<Position>()
        if(dest.hasComponent<MovementComponent>()) {
            val movComp = dest.component<MovementComponent>()
            if(movComp.isMoving)
                return false
        }
        val loc = dest.component<Position>()
        val size: Int = dest.worldSize
        return isStandingIn(
            l.x,
            l.y,
            player.worldSize,
            player.worldSize,
            loc.x,
            loc.y,
            size,
            size
        )
    }

    private fun isStandingIn(
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

}