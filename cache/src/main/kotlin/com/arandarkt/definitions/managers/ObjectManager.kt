package com.arandarkt.definitions.managers

import com.arandarkt.definitions.ObjectDefinition
import com.arandarkt.definitions.loaders.ObjectLoader
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled

class ObjectManager {

    private val loader = ObjectLoader()

    val definitions = mutableMapOf<Int, ObjectDefinition>()

    fun load0(buffer: ByteBuf): ObjectDefinition {
        return loader.load(buffer)
    }

    fun load(id: Int, buffer: ByteBuf = Unpooled.EMPTY_BUFFER): ObjectDefinition {
        if(buffer === Unpooled.EMPTY_BUFFER) {
            return definitions[id] ?: error("Unknown object definition $id")
        }
        return definitions.getOrPut(id) { load0(buffer).also { it.id = id } }
    }
}