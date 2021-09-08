package com.arandarkt.game.api.world

fun interface WorldEvent {

    suspend fun onTick(currentTick: Long)

}