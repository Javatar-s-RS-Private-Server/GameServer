package com.arandarkt.game.api.entity.components.player

import com.arandarkt.game.api.entity.Component
import com.arandarkt.game.api.world.location.components.PositionComponent
import com.arandarkt.game.api.world.map.Direction

class MovementComponent : Component {

    var walkingDirection: Int = -1
    var runningDirection: Int = -1

    var direction: Direction = Direction.NORTH

    override suspend fun onTick(currentTick: Long) {

    }
}