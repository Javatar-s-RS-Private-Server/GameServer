package com.arandarkt.game.api.components.entity.objects

import com.arandarkt.definitions.ObjectDefinition
import com.arandarkt.definitions.managers.ObjectManager
import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.koin.inject
import com.displee.cache.CacheLibrary
import io.netty.buffer.Unpooled

class ObjectDefinitionComponent(val id: Int) : Component {

    val manager: ObjectManager by inject()
    private val cache: CacheLibrary by inject()

    val def: ObjectDefinition
        get() = if (manager.definitions.containsKey(id)) {
            manager.load(id)
        } else {
            val data = cache.data(2, 6, id)
            if (data != null) {
                manager.load(id, Unpooled.wrappedBuffer(cache.data(2, 6, id)))
            } else {
                ObjectDefinition().also { it.id = id }
            }
        }

    fun hasAction(action: String) : Boolean {
        for (a in def.actions) {
            if(a == action) {
                return true
            }
        }
        return false
    }

    fun hasActions(): Boolean {
        if (def.configChangeDest == null) {
            return def.hasOptions(false)
        }
        for (j in def.configChangeDest!!) {
            if (j != -1) {
                val def = loadIfAbsent(j)
                if (def.hasOptions(false)) {
                    return true
                }
            }
        }
        return def.hasOptions(false)
    }

    fun ObjectDefinition.hasOptions(examine: Boolean): Boolean {
        if (name == "null" || actions.count { it == "hidden" } >= actions.size) {
            return false
        }
        for (option in actions) {
            if (option != "hidden" && option != "null") {
                if (examine || option != "Examine") {
                    return true
                }
            }
        }
        return false
    }

    fun loadIfAbsent(id: Int) : ObjectDefinition {
        if(manager.definitions.containsKey(id)) {
            return manager.definitions[id]!!
        }
        return manager.load(id, Unpooled.wrappedBuffer(cache.data(2, 6, id)))
    }

}