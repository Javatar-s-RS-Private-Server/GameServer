package com.arandarkt.game.api.components.entity.player

import com.arandarkt.game.api.components.Component

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