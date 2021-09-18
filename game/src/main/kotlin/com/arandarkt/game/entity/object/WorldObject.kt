package com.arandarkt.game.entity.`object`

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.components.ComponentManager
import com.arandarkt.game.api.components.entity.objects.ObjectDefinitionComponent
import com.arandarkt.game.api.components.entity.objects.SpawnComponent
import com.arandarkt.game.api.entity.`object`.GameObject
import com.arandarkt.game.api.entity.with
import com.arandarkt.game.api.world.location.components.Position

class WorldObject(id: Int, pos: Position = Position(0, 0, 0), type: Int = 10, rotation: Int = 0) : GameObject {
    override val components: ComponentManager<Component> = ComponentManager()
    override val transformations: Array<GameObject?>
        get() = Array(0) { null }

    init {
        with(pos)
        with(ObjectDefinitionComponent(id))
        with(SpawnComponent(type, rotation))
    }

    override suspend fun onTick(tick: Long) {
        for (component in components) {
            component.onTick(tick)
        }
    }
}