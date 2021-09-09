package com.arandarkt.game.api.entity.components.player

import com.arandarkt.game.api.entity.Component
import com.arandarkt.game.api.world.location.components.PositionComponent
import com.arandarkt.game.api.world.map.Direction

class ForcedMovementComponent : Component {

    var forcedStartPosition = PositionComponent.VOID_LOCATION
    var forcedEndPosition = PositionComponent.VOID_LOCATION
    var commenceSpeed: Int = 10
    var pathSpeed: Int = 0
    var direction = Direction.NORTH

}