package com.arandarkt.game.api.entity.character.player.widget

interface GameWidget : ParentWidget {

    val parent: ParentWidget

    val hash: Int
        get() = parent.id shl 16 and id

}