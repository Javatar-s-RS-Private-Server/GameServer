package com.arandarkt.game.api.entity.collection.container

import com.arandarkt.game.api.entity.item.GameItem

interface ItemContainer : Container<GameItem> {

    fun addItem(item: GameItem) : ContainerEvent
    fun removeItem(item: GameItem) : ContainerEvent
    fun item(slot: Int) : GameItem
    fun nextSlot(): Int
    fun isFull(): Boolean
    fun hasAmount(item: GameItem) : Boolean

    fun toList(): List<GameItem>

}