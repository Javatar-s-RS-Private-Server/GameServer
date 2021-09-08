package com.arandarkt.game.api.world.location

import com.arandarkt.game.api.entity.ComponentManager
import com.arandarkt.game.api.world.location.components.IndexedComponent
import com.arandarkt.game.api.world.location.components.PositionComponent

class Location {

    val components = ComponentManager()

    val isIndexed: Boolean get() = components.hasComponent<IndexedComponent>()
    val isPositioned: Boolean get() = components.hasComponent<PositionComponent>()

    val position: PositionComponent get() = components.component()
    val containerIndex: Int get() = components.component<IndexedComponent>().index

    fun position(x: Int, y: Int, z: Int) = apply { components.with(PositionComponent(x, y, z)) }
    fun index(index: Int = -1) = apply { components.with(IndexedComponent(index)) }

}