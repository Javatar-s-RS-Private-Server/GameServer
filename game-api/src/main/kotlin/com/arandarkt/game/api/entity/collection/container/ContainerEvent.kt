package com.arandarkt.game.api.entity.collection.container

import com.arandarkt.game.api.entity.item.GameItem

class ContainerEvent(val isFull: Boolean = false, val invalid: Boolean = false, val added: List<GameItem> = emptyList(), val removed: List<GameItem> = emptyList()) {

    val isDirty: Boolean
        get() = added.isNotEmpty() || removed.isNotEmpty()

    override fun toString(): String {
        return "ContainerEvent(isFull=$isFull, invalid=$invalid, added=$added, removed=$removed, isDirty=$isDirty)"
    }


}