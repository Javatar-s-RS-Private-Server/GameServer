package com.arandarkt.definitions.managers

import com.arandarkt.definitions.MapFloorDefinition
import com.arandarkt.definitions.loaders.MapFloorLoader
import io.netty.buffer.ByteBuf

class MapFloorManager {

    val definitions = mutableMapOf<Int, MapFloorDefinition>()

    val loader = MapFloorLoader()

    fun load0(buffer: ByteBuf) : MapFloorDefinition {
        return loader.load(buffer)
    }

    fun load(id: Int, buffer: ByteBuf) : MapFloorDefinition {
        return definitions.getOrPut(id) { load0(buffer) }
    }

}