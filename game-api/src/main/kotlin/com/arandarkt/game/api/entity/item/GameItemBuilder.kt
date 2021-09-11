package com.arandarkt.game.api.entity.item

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.components.ComponentManager
import com.arandarkt.game.api.components.entity.items.IdentificationComponent
import com.arandarkt.game.api.world.location.components.IndexedComponent
import com.arandarkt.game.api.world.location.components.PositionComponent

interface GameItemBuilder {

    val components: ComponentManager<Component>

    fun item(itemId: Int, amount: Int) = apply { with(IdentificationComponent(itemId, amount)) }

    fun with(component: Component) = apply { components.with(component) }

    fun build() : GameItem

    fun withPosition(x: Int, y: Int, z: Int) : GameItemBuilder = apply { with(PositionComponent(x, y, z)) }

    fun withIndex(index: Int) = apply { with(IndexedComponent(index)) }

}