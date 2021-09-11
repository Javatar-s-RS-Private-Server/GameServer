package com.arandarkt.game.api.components.entity.player

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.components.ComponentManager
import io.guthix.buffer.BitBuf

class SerializationComponent(val components: ComponentManager<Component>) : Component {
    override fun BitBuf.save() {
        for (component in components) {
            if(component === this@SerializationComponent)
                continue
            with(component) {
                save()
            }
        }
    }

    override fun BitBuf.load() {
        for (component in components) {
            if(component === this@SerializationComponent)
                continue
            with(component) {
                load()
            }
        }
    }
}