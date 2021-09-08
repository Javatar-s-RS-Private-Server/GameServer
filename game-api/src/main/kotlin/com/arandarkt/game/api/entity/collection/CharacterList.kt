package com.arandarkt.game.api.entity.collection

import com.arandarkt.game.api.entity.character.Character

interface CharacterList<T : Character> : EntityList<T> {

    fun addEntity(entity: T) : Int
    fun removeEntity(entity: T) : Boolean

    fun shuffle()

}