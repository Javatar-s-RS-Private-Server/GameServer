package com.arandarkt.game.api.world.commands

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.components.ComponentManager

interface GameCommandManager {
    val components: ComponentManager<CommandComponent>
}