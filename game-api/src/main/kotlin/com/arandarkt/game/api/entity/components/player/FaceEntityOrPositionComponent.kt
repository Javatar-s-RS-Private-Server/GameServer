package com.arandarkt.game.api.entity.components.player

import com.arandarkt.game.api.entity.Component
import com.arandarkt.game.api.world.location.components.PositionComponent

class FaceEntityOrPositionComponent : Component {

    var entityIndex = -1
    var position = PositionComponent.VOID_LOCATION

}