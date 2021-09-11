package com.arandarkt.game.api.components.entity.player

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.world.location.components.PositionComponent

class FaceEntityOrPositionComponent : Component {

    var entityIndex = -1
    var position = PositionComponent.VOID_LOCATION

}