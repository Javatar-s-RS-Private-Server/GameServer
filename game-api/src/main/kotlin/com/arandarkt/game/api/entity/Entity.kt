package com.arandarkt.game.api.entity

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.components.ComponentManager

interface Entity {

    val components: ComponentManager<Component>
    val worldSize: Int

    suspend fun onTick(tick: Long)

}