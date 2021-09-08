package com.arandarkt.game.entity.collection.character

import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.entity.collection.CharacterList
import com.arandarkt.game.entity.collection.EntityList

abstract class CharacterList<C : Character> : EntityList<C>(), CharacterList<C>