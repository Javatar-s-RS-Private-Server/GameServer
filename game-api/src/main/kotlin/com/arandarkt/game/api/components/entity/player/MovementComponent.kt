package com.arandarkt.game.api.components.entity.player

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.world.map.Direction

class MovementComponent : Component {

    var walkingDirection: Int = -1
    var runningDirection: Int = -1

    var direction: Direction = Direction.SOUTH

    override suspend fun onTick(currentTick: Long) {

    }
}