package com.arandarkt.game.api.components.entity.player

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.world.map.MapRegion

class ViewportComponent(player: PlayerCharacter, region: MapRegion) : Component {
    val localPlayers = mutableListOf<PlayerCharacter>()

    var region: MapRegion = region
}