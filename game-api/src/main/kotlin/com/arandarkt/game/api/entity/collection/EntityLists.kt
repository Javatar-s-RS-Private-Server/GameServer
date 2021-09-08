package com.arandarkt.game.api.entity.collection

import com.arandarkt.game.api.entity.character.player.PlayerCharacter

interface EntityLists {

    fun newPlayerList(size: Int = 2000) : CharacterList<PlayerCharacter>

    fun newEntityList(size: Int = 2000) : EntityList<in PlayerCharacter>

}