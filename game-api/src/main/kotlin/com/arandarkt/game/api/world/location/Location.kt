package com.arandarkt.game.api.world.location

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.components.ComponentManager
import com.arandarkt.game.api.world.location.components.IndexedComponent
import com.arandarkt.game.api.world.location.components.Position

class Location {

    val components = ComponentManager<Component>()

    val isIndexed: Boolean get() = components.hasComponent<IndexedComponent>()
    val isPositioned: Boolean get() = components.hasComponent<Position>()

    val position: Position get() = components.component()
    val containerIndex: Int get() = components.component<IndexedComponent>().index

    fun position(x: Int, y: Int, z: Int) = apply { components.with(Position(x, y, z)) }
    fun index(index: Int = -1) = apply { components.with(IndexedComponent(index)) }

}