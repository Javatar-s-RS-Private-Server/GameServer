package com.arandarkt.game.api.entity

interface Entity {

    val components: ComponentManager

    suspend fun onTick(tick: Long)

}