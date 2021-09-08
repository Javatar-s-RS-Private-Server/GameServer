package com.arandarkt.game.api.entity.`object`

import com.arandarkt.game.api.entity.Entity

interface GameObject : Entity {

    val transformations: Array<GameObject>

}