package com.arandarkt.game.api.entity.components.items

import com.arandarkt.game.api.entity.Component

class DefinitionComponent(
    val stackable: Boolean = false,
    val removeSleeves: Boolean = false,
    val removeHead: Boolean = false,
    val removeBeard: Boolean = false
) : Component {

    override fun copy(): Component {
        return DefinitionComponent(this.stackable)
    }
}