package com.arandarkt.game.api.entity.components.player

import com.arandarkt.game.api.entity.ComponentManager
import com.arandarkt.game.api.entity.collection.container.ItemContainer
import com.arandarkt.game.api.entity.item.GameItemBuilder
import com.arandarkt.game.api.io.array
import com.arandarkt.game.api.koin.newDefinedItem
import com.arandarkt.game.entity.collection.container.Inventory
import com.arandarkt.game.entity.item.ItemBuilder
import io.guthix.buffer.toBitMode
import io.netty.buffer.Unpooled
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Ignore

class ComponentSerializationTest {

    @Test
    @Ignore
    fun `inventory serialization test`() {
        val comps = ComponentManager()
        comps.with(ItemContainerComponent())
        comps.with(SerializationComponent(comps))

        val save = comps.component<SerializationComponent>()

        val cons = comps.component<ItemContainerComponent>()

        val inventory = cons.inventory

        val whip = newDefinedItem(4151).build()

        inventory.addItem(whip)

        val buffer = Unpooled.buffer().toBitMode()

        with(save) { buffer.save() }

        Files.write(Path.of("/home/javatar/IdeaProjects/ArandarKt/test/test.dat"), buffer.array())

    }

    @Test
    @Ignore
    fun `load test`() {
        val comps = ComponentManager()
        comps.with(ItemContainerComponent())
        comps.with(SerializationComponent(comps))

        val data = Files.readAllBytes(Path.of("/home/javatar/IdeaProjects/ArandarKt/test/test.dat"))

        println(data.size)

        val buffer = Unpooled.wrappedBuffer(data).toBitMode()

        val load = comps.component<SerializationComponent>()

        with(load) { buffer.load() }

        val cons = comps.component<ItemContainerComponent>()

        val inventory = cons.inventory

        println(inventory)

    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun init() {
            startKoin {
                modules(module {
                    factory { ItemBuilder() } bind GameItemBuilder::class
                    factory(named("inv")) { Inventory() } bind ItemContainer::class
                    factory(named("equip")) { Inventory() } bind ItemContainer::class
                    factory(named("bank")) { Inventory() } bind ItemContainer::class
                })
            }
        }
    }

}