package com.arandarkt.definitions.managers

import com.arandarkt.definitions.MapFloorDefinition
import com.arandarkt.definitions.ObjectPlacementDefinition
import com.arandarkt.definitions.loaders.ObjectPlacementLoader
import io.netty.buffer.ByteBuf

class ObjectPlacementManager {

    val definitions = mutableMapOf<Int, ObjectPlacementDefinition>()

    val loader = ObjectPlacementLoader()

    fun load0(buffer: ByteBuf, floorDef: MapFloorDefinition) : ObjectPlacementDefinition {
        return loader.load(buffer, floorDef)
    }

    fun load(id: Int, buffer: ByteBuf, floorDef: MapFloorDefinition) : ObjectPlacementDefinition {
        return definitions.getOrPut(id) { load0(buffer, floorDef) }
    }

}