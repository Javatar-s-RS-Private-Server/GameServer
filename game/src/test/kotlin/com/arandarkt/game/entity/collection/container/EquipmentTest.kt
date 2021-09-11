package com.arandarkt.game.entity.collection.container

import com.arandarkt.game.api.components.entity.items.EquipmentComponent
import com.arandarkt.game.api.components.entity.player.apperance.AppearanceComponent
import com.arandarkt.game.api.entity.item.GameItemBuilder
import com.arandarkt.game.api.koin.newEquipment
import com.arandarkt.game.entity.item.ItemBuilder
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module

class EquipmentTest {

    @Test
    fun `equip one handed item`() {
        val equipment = Equipment()

        val whip = newEquipment(4151).build()

        equipment.addItem(whip)

        assert(equipment.items[AppearanceComponent.SLOT_WEAPON].itemId == 4151) { "Failed to equip whip." }

    }

    @Test
    fun `equip two handed item`() {
        val equipment = Equipment()

        val whip = newEquipment(4151).with(EquipmentComponent(true)).build()
        val shield = newEquipment(4153, slot = AppearanceComponent.SLOT_SHIELD).build()
        equipment.addItem(shield)
        equipment.addItem(whip)

        assert(equipment.items[AppearanceComponent.SLOT_WEAPON].itemId == 4151) { "Failed to equip whip two handed." }
        assert(equipment.items[AppearanceComponent.SLOT_SHIELD].itemId == -1) { "Failed to equip shield." }

        equipment.addItem(shield)

        assert(equipment.items[AppearanceComponent.SLOT_WEAPON].itemId == -1) { "Failed to de-equip the weapon." }
        assert(equipment.items[AppearanceComponent.SLOT_SHIELD].itemId == 4153) { "Failed to equip shield." }
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun init() {
            startKoin {
                modules(module {
                    factory { ItemBuilder() } bind GameItemBuilder::class
                })
            }
        }

        @JvmStatic
        @AfterAll
        fun stop() {
            GlobalContext.stopKoin()
        }

    }

}