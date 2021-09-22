package com.arandarkt.definitions.loaders

import com.arandarkt.definitions.MapFloorDefinition
import com.arandarkt.definitions.ObjectPlacementDefinition
import io.guthix.buffer.readUnsignedSmallSmart
import io.netty.buffer.ByteBuf

class ObjectPlacementLoader {

    fun load(buffer: ByteBuf, floorDef: MapFloorDefinition) : ObjectPlacementDefinition {
        val def = ObjectPlacementDefinition()
        var objectId = -1
        while (true) {
            var offset: Int = buffer.readUnsignedSmallSmart()
            if (offset == 0) {
                break
            }
            objectId += offset
            var location = 0
            while (true) {
                offset = buffer.readUnsignedSmallSmart()
                if (offset == 0) {
                    break
                }
                location += offset - 1
                val y = location and 0x3f
                val x = location shr 6 and 0x3f
                val configuration: Int = buffer.readUnsignedByte().toInt()
                val rotation = configuration and 0x3
                val type = configuration shr 2
                var z = location shr 12
                if (floorDef.mapscape[1][x][y].toInt() and 0x2 == 2) {
                    z--
                }
                if (z in 0..3) {
                    def.objects.add(ObjectPlacementDefinition.ObjectSpawn(objectId, x, y, rotation, type, z))
                }
            }
        }
        return def
    }

}