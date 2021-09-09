package com.arandarkt.game.entity.collection.container

import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.entity.item.GameItemBuilder
import com.arandarkt.game.api.koin.newDefinedItem
import com.arandarkt.game.api.koin.newStackedItem
import com.arandarkt.game.api.world.location.components.IndexedComponent
import com.arandarkt.game.entity.item.ItemBuilder
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.test.Ignore

class InventoryTest {

    @Test
    fun `add item to inventory`() {
        val inventory = Inventory()
        val whip = newDefinedItem(4151, 1).build()

        val event = inventory.addItem(whip)

        assert(whip.component<IndexedComponent>().index == 0) { "Failed to update location." }
        assert(inventory.contains(whip)) { "Failed to add whip to container." }
        assert(event.added.contains(whip)) { "Failed to create event for adding of whip." }

        val whips = newDefinedItem(4151, 3).build()

        val addEvent = inventory.addItem(whips)

        assert(inventory.count { it.itemId == 4151 } == 4)
        assert(addEvent.added.contains(whips)) { "FAiled to create adding of multiple whips." }

    }

    @Test
    fun `add stackable item`() {
        val inventory = Inventory()
        val coins = newStackedItem(995, 1000).build()

        println(coins.itemId)
        println(coins.amount)

        val event = inventory.addItem(coins)

        assert(event.added.contains(coins))
        assert(inventory.hasAmount(coins)) { "Failed to add stackable item." }

        val moreCoins = newStackedItem(995, 500).build()

        val e = inventory.addItem(moreCoins)

        println(inventory)

        assert(e.added.contains(moreCoins)) { "Failed to create second stacked event." }
        assert(inventory.hasAmount(moreCoins.copy(1500))) { "Failed to add more coins." }

    }

    @Test
    fun `remove item`() {
        val inventory = Inventory()
        val whip = newDefinedItem(4151).build()

        val event = inventory.addItem(whip)

        assert(inventory.contains(whip)) { "Failed to add whip to container." }
        assert(event.added.contains(whip)) { "Failed to create event for adding of whip." }

        val revent = inventory.removeItem(whip)

        assert(!inventory.contains(whip)) { "Failed to remove whip to container." }
        assert(revent.removed.contains(whip)) { "Failed to create event for removing of whip." }

        inventory.addItem(whip.copy(5))

        assert(inventory.count { it.itemId == 4151 } == 5) { "Failed to add whips." }

        inventory.removeItem(newDefinedItem(4151,3).build())

        assert(inventory.count { it.itemId == 4151 } == 2) { "Failed to remove whips." }

    }

    @Test
    fun `remove stackable item`() {
        val inventory = Inventory()
        val coins = newStackedItem(995, 5000).build()

        inventory.addItem(coins)

        assert(inventory.contains(coins)) { "Failed to add 5000 coins." }

        val moreCoins = newStackedItem(995, 2500).build()

        inventory.removeItem(moreCoins)

        assert(inventory.items[coins.component<IndexedComponent>().index].amount == 2500) { "Failed to remove coins." }

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