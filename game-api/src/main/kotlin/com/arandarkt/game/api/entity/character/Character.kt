package com.arandarkt.game.api.entity.character

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.entity.Entity
import kotlinx.coroutines.flow.MutableStateFlow

interface Character : Entity {

    val index: Int

    val action: MutableStateFlow<Component>

}