package com.arandarkt.game.api.components

import io.guthix.buffer.BitBuf

interface Component {
    suspend fun onTick(currentTick: Long) : Boolean {
        return false
    }
    fun BitBuf.save() {}
    fun BitBuf.load() {}
    fun copy() = this

    fun isEqual(comp: Component) : Boolean {
        return true
    }


}