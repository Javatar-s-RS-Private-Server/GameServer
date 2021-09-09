package com.arandarkt.game.api.entity

import com.arandarkt.game.api.entity.ComponentManager.Companion.EMPTY_COMPONENT
import kotlin.reflect.KClass

@JvmName("withComponent")
inline fun <reified E : Entity> E.with(component: Component) = apply {
    components.with(component)
}

fun Entity.with(vararg comps : Component) = apply {
    comps.forEach { components.with(it) }
}

inline fun <reified C : Component> Entity.component() : C = components.component()

inline fun <reified C : Component> Entity.hasComponent() = components.hasComponent<C>()

fun Entity.getOrCreateComponent(clazz: KClass<*>, supplier: () -> Component = { EMPTY_COMPONENT }) : Component {
    return if(!components.components.containsKey(clazz)) {
        val comp = supplier()
        components.with(comp)
        comp
    } else {
        components.components[clazz]!!
    }
}

inline fun <reified C: Component> Entity.getOrCreate(supplier: () -> C) : C {
    return if(!components.components.containsKey(C::class)) {
        val comp = supplier()
        components.with(comp)
        comp
    } else {
        components.components[C::class] as C
    }
}