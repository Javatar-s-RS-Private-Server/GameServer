package com.arandarkt.game.api.components.entity.items

import com.arandarkt.game.api.components.Component

class ItemDefinitionComponent(
    val stackable: Boolean = false,
    val removeSleeves: Boolean = false,
    val removeHead: Boolean = false,
    val removeBeard: Boolean = false
) : Component {

    override fun copy(): Component {
        return ItemDefinitionComponent(this.stackable)
    }
}