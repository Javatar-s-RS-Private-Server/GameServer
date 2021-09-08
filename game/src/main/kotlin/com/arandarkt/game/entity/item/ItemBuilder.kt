package com.arandarkt.game.entity.item

import com.arandarkt.game.api.entity.ComponentManager
import com.arandarkt.game.api.entity.item.GameItem
import com.arandarkt.game.api.entity.item.GameItemBuilder

class ItemBuilder : GameItemBuilder {
    override val components: ComponentManager = ComponentManager()

    override fun build(): GameItem {
        return Item(components)
    }
}