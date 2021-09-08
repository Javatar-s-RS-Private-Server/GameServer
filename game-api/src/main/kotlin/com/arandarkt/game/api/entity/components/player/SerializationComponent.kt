package com.arandarkt.game.api.entity.components.player

import com.arandarkt.game.api.entity.Component
import com.arandarkt.game.api.entity.ComponentManager
import io.guthix.buffer.BitBuf
import io.netty.buffer.ByteBuf

class SerializationComponent(val components: ComponentManager) : Component {
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