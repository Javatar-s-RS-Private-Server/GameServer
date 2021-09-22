package com.arandarkt.game.api.entity

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.components.ComponentManager
import kotlin.reflect.KClass

@JvmName("withComponent")
inline infix fun <reified E : Entity> E.with(component: Component) : Pair<ComponentManager<Component>, Component> {
    components.with(component)
    return components to component
}

inline infix fun <reified B : Component> Pair<ComponentManager<B>, B>.bind(kClass: KClass<*>) {
    val manager = first
    val comp = second
    manager.components[kClass] = comp
}

inline operator fun <reified E : Entity> E.plus(component: Component) : E {
    components.with(component)
    return this
}

inline fun <reified C : Component> Entity.component() : C = components.component()

inline fun <reified C : Component> Entity.hasComponent() = components.hasComponent<C>()

inline fun <reified C : Component> Entity.with(supplier: () -> C) : C {
    return if(!components.components.containsKey(C::class)) {
        val comp = supplier()
        components.with(comp)
        comp
    } else {
        components.components[C::class] as C
    }
}