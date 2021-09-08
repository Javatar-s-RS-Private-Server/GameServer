package com.arandarkt.game.api.entity

import kotlin.reflect.KClass

class ComponentManager : Iterable<Component> {

    val components = mutableMapOf<KClass<*>, Component>()

    fun with(component: Component) = apply {
        components[component::class] = component
    }

    fun getComponent(clazz: KClass<*>) : Component {
        return components[clazz] ?: EMPTY_COMPONENT
    }

    fun hasComponent(clazz: KClass<*>) : Boolean = components.containsKey(clazz)

    inline fun <reified C : Component> component() : C {
        return getComponent(C::class) as C
    }

    inline fun <reified C: Component> replace(component: C) = with(component)

    inline fun <reified C : Component> hasComponent(): Boolean = hasComponent(C::class)

    override fun iterator(): Iterator<Component> {
        return components.values.iterator()
    }

    companion object {
        val EMPTY_COMPONENT = object : Component {}
    }

}