package com.arandarkt.game.entity.collection.container

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.components.ComponentManager
import com.arandarkt.game.api.entity.collection.container.ContainerEvent
import com.arandarkt.game.api.entity.collection.container.ItemContainer
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.components.entity.items.DefinitionComponent
import com.arandarkt.game.api.components.entity.items.IdentificationComponent
import com.arandarkt.game.api.entity.item.GameItem
import com.arandarkt.game.api.entity.with
import com.arandarkt.game.api.koin.emptyItem
import com.arandarkt.game.api.world.location.components.IndexedComponent
import com.arandarkt.game.entity.item.Item

class Inventory(override val size: Int = 28) : ItemContainer {

    val items = Array<GameItem>(size) {
        Item(
            ComponentManager<Component>()
                .with(IdentificationComponent(-1, 0))
                .with(IndexedComponent(-1))
        )
    }

    override fun addItem(item: GameItem): ContainerEvent {
        if (!item.components.hasComponent<DefinitionComponent>())
            return ContainerEvent(invalid = true)
        if (isFull())
            return ContainerEvent(true)
        val def = item.component<DefinitionComponent>()

        if (def.stackable) {
            return addStackableItem(item)
        }

        if (item.amount == 1) {
            val index = nextSlot()
            return if (index != -1) {
                items[index] = item.with(IndexedComponent(index))
                ContainerEvent(added = listOf(item))
            } else {
                ContainerEvent(true)
            }
        } else if (item.amount > 1) {
            var added = 0
            repeat(item.amount) {
                val index = nextSlot()
                if (index == -1 && it == 0) {
                    return ContainerEvent(true)
                } else if (index != -1) {
                    items[index] = item.copy(1).with(IndexedComponent(index))
                    added++
                } else if (index == -1)
                    return@repeat
            }
            if (added > 0) {
                return ContainerEvent(added = listOf(item.copy(added)))
            }
        }
        return ContainerEvent()
    }

    private fun addStackableItem(item: GameItem): ContainerEvent {
        val index = items.indexOf(item)
        if (index != -1) {
            val found = items[index]
            val def = found.component<DefinitionComponent>()
            if (def.stackable) {
                val amount = item.amount.toLong()
                val amt = found.amount.toLong()
                return if (amount + amt > Int.MAX_VALUE) {
                    ContainerEvent(true)
                } else {
                    items[index] = found.copy((amount + amt).toInt()).with(IndexedComponent(index))
                    ContainerEvent(added = listOf(items[index]))
                }
            }
        } else {
            val slot = nextSlot()
            return if (slot != -1) {
                items[slot] = item.with(IndexedComponent(slot))
                ContainerEvent(added = listOf(item))
            } else {
                ContainerEvent(true)
            }
        }
        return ContainerEvent(invalid = true)
    }

    override fun removeItem(item: GameItem): ContainerEvent {
        if (isEmpty())
            return ContainerEvent(invalid = true)
        val index = items.indexOf(item)
        if (index != -1) {
            val def = item.component<DefinitionComponent>()
            val found = items[index]
            val fdef = found.component<DefinitionComponent>()
            if (def.stackable && fdef.stackable) {
                val amount = item.amount
                val famount = found.amount
                val newAmount = (famount - amount)
                if (newAmount > 0) {
                    val old = items[index]
                    items[index] = found.copy(newAmount)
                    return ContainerEvent(removed = listOf(old))
                } else if (newAmount <= 0) {
                    val removed = (amount - famount)
                    items[index] = emptyItem()
                    return ContainerEvent(removed = listOf(item.copy(removed)))
                }
            } else if (item.amount == 1) {
                val old = items[index]
                items[index] = emptyItem()
                return ContainerEvent(removed = listOf(old))
            } else if (item.amount > 1) {
                var removed = 1
                val old = items[index]
                items[index] = emptyItem()
                repeat(item.amount - 1) {
                    val removeIndex = items.indexOf(item)
                    items[removeIndex] = emptyItem()
                    removed++
                }
                return ContainerEvent(removed = listOf(old.copy(removed)))
            }
        }
        return ContainerEvent(invalid = true)
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
        val found = items.find { it.itemId == item.itemId } ?: return false
        val def = item.component<DefinitionComponent>()
        val odef = found.component<DefinitionComponent>()
        if (def.stackable && odef.stackable) {
            return item.amount >= found.amount
        }
        return items.count { it.itemId == item.itemId } == item.amount
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

    override fun toString(): String {
        return "Inventory(size=$size, items=${items.contentToString()})"
    }


}