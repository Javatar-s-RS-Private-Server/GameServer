package com.arandarkt.game.entity.collection.container

import com.arandarkt.game.api.entity.collection.container.ContainerEvent
import com.arandarkt.game.api.entity.collection.container.ItemContainer
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.components.entity.items.ItemDefinitionComponent
import com.arandarkt.game.api.components.entity.items.EquipmentComponent
import com.arandarkt.game.api.components.entity.player.apperance.AppearanceComponent
import com.arandarkt.game.api.entity.item.GameItem
import com.arandarkt.game.api.koin.emptyItem
import com.arandarkt.game.api.world.location.components.IndexedComponent

class Equipment(override val size: Int = 14) : ItemContainer {

    val items = Array(size) { emptyItem() }

    override fun addItem(item: GameItem): ContainerEvent {
        val slot = item.component<IndexedComponent>()
        if (slot.index == -1)
            return ContainerEvent(invalid = true)

        val def = item.component<ItemDefinitionComponent>()
        val equipComp = item.component<EquipmentComponent>()
        val equipSlot = slot.index
        val equiped = items[equipSlot]


        val added = if (def.stackable) {
            val amount = item.amount.toLong()
            val amt = equiped.amount.toLong()
            if ((amt + amount) < Int.MAX_VALUE) {
                items[equipSlot] = equiped.copy((amt + amount).toInt())
                listOf(items[equipSlot])
            } else emptyList()
        } else {
            items[equipSlot] = item
            listOf(item)
        }

        val removed =
            if (equipComp.isTwoHanded || (items[AppearanceComponent.SLOT_WEAPON].itemId != -1 && items[AppearanceComponent.SLOT_WEAPON].component<EquipmentComponent>().isTwoHanded)) {
                val toRemoveSlot = if (equipSlot == AppearanceComponent.SLOT_WEAPON) {
                    AppearanceComponent.SLOT_SHIELD
                } else AppearanceComponent.SLOT_WEAPON
                val toRemoveItem = items[toRemoveSlot]
                if (toRemoveItem.itemId != -1) {
                    items[toRemoveSlot] = emptyItem()
                    listOf(toRemoveItem)
                } else emptyList()
            } else {
                emptyList()
            }

        return ContainerEvent(added = added, removed = removed)
    }

    override fun removeItem(item: GameItem): ContainerEvent {
        TODO("Not yet implemented")
    }

    override fun item(slot: Int): GameItem {
        return items[slot]
    }

    override fun nextSlot(): Int {
        return items.indexOfFirst { it.itemId == -1 }
    }

    override fun isFull(): Boolean {
        return items.count { it.itemId == -1 } == 0
    }

    override fun hasAmount(item: GameItem): Boolean {
        TODO("Not yet implemented")
    }

    override fun toList(): List<GameItem> {
        return items.toList()
    }

    override fun contains(element: GameItem): Boolean {
        return items.find { it == element } != null
    }

    override fun containsAll(elements: Collection<GameItem>): Boolean {
        for (item in items) {
            for (element in elements) {
                if (item != element)
                    return false
            }
        }
        return true
    }

    override fun isEmpty(): Boolean {
        return items.count { it.itemId == -1 } == size
    }

    override fun iterator(): Iterator<GameItem> {
        return items.iterator()
    }
}