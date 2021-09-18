package com.arandarkt.game.api.world.map.path

import com.arandarkt.game.api.components.entity.player.MovementComponent
import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.world.map.Point
import java.util.*

class Path {
    var isSuccessful = false
    var shouldMoveNear = false
    val points: Deque<Point> = ArrayDeque()

    fun walk(entity: Character) {
        val mov = entity.component<MovementComponent>()
        if (mov.isLocked) {
            return
        }
        mov.reset()
        for (step in points) {
            mov.addPath(step.x, step.y)
        }
    }

}