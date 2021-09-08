package com.arandarkt.game.api.entity.components.player

import com.arandarkt.game.api.entity.Component
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.world.map.WorldRegion
import kotlinx.coroutines.flow.MutableStateFlow

class ViewportComponent(var region: WorldRegion) : Component {
    val localPlayers = mutableListOf<PlayerCharacter>()

    override suspend fun onTick(currentTick: Long) {}
}