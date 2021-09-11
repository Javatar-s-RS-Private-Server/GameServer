package com.arandarkt.game.api.components.entity.player

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.entity.collection.container.ItemContainer
import com.arandarkt.game.api.koin.get
import io.guthix.buffer.BitBuf
import org.koin.core.qualifier.named

class ItemContainerComponent(val containers: MutableMap<Int, ItemContainer> = mutableMapOf()) : Component {

    val inventory = containers.getOrPut(93) { get(named("inv")) }
    val equipment = containers.getOrPut(94) { get(named("equip")) }
    val bank = containers.getOrPut(95) { get(named("bank")) }

    override fun BitBuf.save() {
        for (container in containers) {
            if(container.value.isEmpty())
                continue
            for (gameItem in container.value.toList()) {
                for (component in gameItem.components) {
                    with(component) { save() }
                }
            }
        }
    }

    override fun BitBuf.load() {
        for (container in containers) {
            for (gameItem in container.value.toList()) {
                for (component in gameItem.components) {
                    with(component) { load() }
                }
            }
        }
    }
}