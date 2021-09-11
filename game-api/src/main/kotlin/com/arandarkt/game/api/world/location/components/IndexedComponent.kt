package com.arandarkt.game.api.world.location.components

import com.arandarkt.game.api.components.Component
import io.guthix.buffer.BitBuf
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