package com.arandarkt.game.entity.collection

import com.arandarkt.game.api.entity.Entity
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.collection.CharacterList
import com.arandarkt.game.api.entity.collection.EntityList
import com.arandarkt.game.api.entity.collection.EntityLists
import com.arandarkt.game.entity.collection.character.impl.PlayerList

class EntityCollections : EntityLists {

    override fun newPlayerList(size: Int): CharacterList<PlayerCharacter> {
        return PlayerList(size)
    }

    override fun newEntityList(size: Int): EntityList<Entity> {
        TODO("Not yet implemented")
    }

}