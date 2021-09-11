package com.arandarkt.game.api.components

import kotlin.reflect.KClass

class ComponentManager<B : Component> : Iterable<B> {

    val components = mutableMapOf<KClass<*>, B>()

    fun getComponent(kClass: KClass<*>) : B? {
        return components[kClass]
    }

    fun with(component: B) = apply {
        components[component::class] = component
    }

    fun hasComponent(clazz: KClass<*>): Boolean = components.containsKey(clazz)

    inline fun <reified C : B> component(): C {
        return components[C::class] as C
    }

    inline fun <reified C : B> replace(component: C) = with(component)

    inline fun <reified C : B> hasComponent(): Boolean = hasComponent(C::class)

    override fun iterator(): Iterator<B> {
        return components.values.iterator()
    }
}