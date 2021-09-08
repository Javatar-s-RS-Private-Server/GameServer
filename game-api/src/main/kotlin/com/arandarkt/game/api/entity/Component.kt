package com.arandarkt.game.api.entity

import io.guthix.buffer.BitBuf

interface Component {
    suspend fun onTick(currentTick: Long) {}
    fun BitBuf.save() {}
    fun BitBuf.load() {}
    fun copy() = this

    fun isEqual(comp: Component) : Boolean {
        return true
    }
}