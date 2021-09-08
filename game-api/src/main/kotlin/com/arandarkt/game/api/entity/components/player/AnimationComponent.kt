package com.arandarkt.game.api.entity.components.player

import com.arandarkt.game.api.entity.Component

class AnimationComponent : Component {

    var animationId: Int = -1
    var animationDelay: Int = -1
    var priority: Priority = Priority.LOW

    companion object {
        enum class Priority {
            LOW,
            MID,
            HIGH,
            VERY_HIGH
        }
    }
}