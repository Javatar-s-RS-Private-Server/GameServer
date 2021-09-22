package com.arandarkt.game.api.components

import kotlin.reflect.KClass

class ComponentManager<BASE : Component> : Iterable<BASE> {

    val components = mutableMapOf<KClass<*>, BASE>()

    fun getComponent(kClass: KClass<*>) : BASE? {
        return components[kClass]
    }

    fun with(component: BASE) = apply {
        components[component::class] = component
    }

    fun hasComponent(clazz: KClass<*>): Boolean = components.containsKey(clazz)

    inline fun <reified C : BASE> with(component: C) {
        components[C::class] = component
    }

    inline fun <reified C : BASE> component(): C {
        return components[C::class] as C
    }

    inline fun <reified C : BASE> replace(component: C) = with(component)

    inline fun <reified C : BASE> hasComponent(): Boolean = hasComponent(C::class)

    override fun iterator(): Iterator<BASE> {
        return components.values.iterator()
    }
}