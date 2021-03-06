package com.arandarkt.game.api.components.entity.player

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.world.location.components.Position
import com.arandarkt.game.api.world.map.Direction

class ForcedMovementComponent : Component {

    var forcedStartPosition = Position.VOID_LOCATION
    var forcedEndPosition = Position.VOID_LOCATION
    var commenceSpeed: Int = 10
    var pathSpeed: Int = 0
    var direction = Direction.NORTH

}