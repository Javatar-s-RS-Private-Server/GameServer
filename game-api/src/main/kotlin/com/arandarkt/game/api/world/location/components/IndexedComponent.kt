package com.arandarkt.game.api.world.location.components

import com.arandarkt.game.api.entity.Component
import io.guthix.buffer.BitBuf
import io.guthix.buffer.toBitMode
import io.netty.buffer.ByteBuf
import kotlin.reflect.KProperty

class IndexedComponent(var index: Int) : Component {

    operator fun getValue(ref: Any?, property: KProperty<*>) : Int {
        return index
    }

    override fun BitBuf.save() {
        writeBoolean(index != -1)
        if(index != -1) {
            writeBits(index, 9)
        }
    }

    override fun BitBuf.load() {
        if(readBoolean())
            index = readBits(9)
    }
}