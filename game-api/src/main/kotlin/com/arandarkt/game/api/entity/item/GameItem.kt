package com.arandarkt.game.api.entity.item

import com.arandarkt.game.api.entity.Entity

interface GameItem : Entity {

    val itemId: Int
    val amount: Int

    fun copy(amount: Int = this.amount) : GameItem

    companion object {
        fun GameItem.isEmpty() = itemId == -1
        fun GameItem.isNotEmpty() = !isEmpty()
    }
}
