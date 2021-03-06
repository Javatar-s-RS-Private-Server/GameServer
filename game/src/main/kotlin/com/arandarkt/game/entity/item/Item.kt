package com.arandarkt.game.entity.item

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.components.ComponentManager
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.components.entity.items.IdentificationComponent
import com.arandarkt.game.api.entity.item.GameItem

class Item(
    override val components: ComponentManager<Component> = ComponentManager()
) : GameItem {
    override val itemId: Int by component<IdentificationComponent>()
    override val amount: Int by component<IdentificationComponent>()
    override val worldSize: Int = 1

    override suspend fun onTick(tick: Long) {
        for (component in components) {
            component.onTick(tick)
        }
    }

    override fun copy(amount: Int): GameItem {
        val comps = ComponentManager<Component>()
        for (component in components) {
            comps.with(component.copy())
        }
        comps.replace(IdentificationComponent(itemId, amount))
        return Item(comps)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (itemId != other.itemId) return false

        for (comp in components) {
            for (otherComp in other.components) {
                if(!comp.isEqual(otherComp))
                    return false
            }
        }

        return true
    }

    override fun hashCode(): Int {
        return components.hashCode()
    }

    override fun toString(): String {
        return "Item(itemId=$itemId, amount=$amount)"
    }


}