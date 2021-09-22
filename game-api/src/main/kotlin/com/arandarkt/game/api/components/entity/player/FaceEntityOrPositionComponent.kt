package com.arandarkt.game.api.components.entity.player

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.world.location.components.Position

class FaceEntityOrPositionComponent : Component {

    var entityIndex = -1
    var position = Position.VOID_LOCATION

    fun shouldFaceEntity() = entityIndex != -1
    fun shouldFacePosition() = position !== Position.VOID_LOCATION

}