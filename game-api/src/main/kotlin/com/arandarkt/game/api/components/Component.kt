package com.arandarkt.game.api.components

import io.guthix.buffer.BitBuf

interface Component {
    suspend fun onTick(currentTick: Long) {}
    fun BitBuf.save() {}
    fun BitBuf.load() {}
    fun copy() = this

    fun isEqual(comp: Component) : Boolean {
        return true
    }

    companion object {
        val VOID_ACTION = object : Component {  }
    }
}