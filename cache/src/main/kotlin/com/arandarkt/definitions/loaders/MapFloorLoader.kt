package com.arandarkt.definitions.loaders

import com.arandarkt.definitions.MapFloorDefinition
import io.netty.buffer.ByteBuf

class MapFloorLoader {

    fun load(buffer: ByteBuf): MapFloorDefinition {
        val def = MapFloorDefinition()
        for (z in 0..3) {
            for (x in 0..63) {
                for (y in 0..63) {
                    while (true) {
                        val value: Int = buffer.readUnsignedByte().toInt()
                        if (value == 0) {
                            break
                        }
                        if (value == 1) {
                            buffer.skipBytes(1)
                            break
                        }
                        if (value <= 49) { //Overlay data
                            val overlayData: Int = buffer.readUnsignedByte().toInt()
                            if (overlayData != 42 && overlayData > 0) {
                                def.landscape[z][x][y] = true
                            }
                        } else if (value <= 81) {
                            def.mapscape[z][x][y] = (value - 49).toByte()
                        } else {
                            val underlayData: Int = (value - 81) and 0xFF //Underlay data
                            if (underlayData != 42 && underlayData > 0) {
                                def.landscape[z][x][y] = true
                            }
                        }
                    }
                }
            }
        }
        return def
    }

}