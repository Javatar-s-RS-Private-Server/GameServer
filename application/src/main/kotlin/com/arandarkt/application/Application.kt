package com.arandarkt.application

import com.arandarkt.game.api.database.ArandarDatabaseManager
import com.arandarkt.game.api.database.player.PlayerEntityService
import com.arandarkt.game.api.entity.collection.EntityLists
import com.arandarkt.game.api.entity.collection.container.ItemContainer
import com.arandarkt.game.api.entity.item.GameItemBuilder
import com.arandarkt.game.api.koin.get
import com.arandarkt.game.api.packets.packetModule
import com.arandarkt.game.api.world.Ticker
import com.arandarkt.game.api.world.World
import com.arandarkt.game.api.world.map.GameRegionManager
import com.arandarkt.game.entity.collection.EntityCollections
import com.arandarkt.game.entity.collection.container.Equipment
import com.arandarkt.game.entity.collection.container.Inventory
import com.arandarkt.game.entity.item.ItemBuilder
import com.arandarkt.game.world.GameWorld
import com.arandarkt.game.world.PlayerFactory
import com.arandarkt.game.world.WorldTicker
import com.arandarkt.game.world.events.LoginEvents
import com.arandarkt.game.world.events.PlayerShuffleEvent
import com.arandarkt.game.world.events.PlayersEvent
import com.arandarkt.game.world.map.RegionManager
import com.arandarkt.network.ArandarServer
import com.arandarkt.xtea.XteaManager
import com.arandarkt.xtea.loaders.FileLineXteaLoader
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import java.net.InetSocketAddress
import java.nio.file.Path

object Application {

    @JvmStatic
    fun main(args: Array<String>) {

        startKoin {
            modules(module {
                single { PlayerEntityService() }
                single { ArandarDatabaseManager() }
                single { RegionManager() } bind GameRegionManager::class
                single { WorldTicker() } bind Ticker::class
                single { GameWorld() } bind World::class
                single { PlayerFactory() } bind com.arandarkt.game.api.world.PlayerFactory::class
                single { EntityCollections() } bind EntityLists::class
                single { XteaManager(FileLineXteaLoader()) }
                factory { ItemBuilder() } bind GameItemBuilder::class
            }, packetModule,  module {
                factory(named("inv")) { Inventory() } bind ItemContainer::class
                factory(named("equip")) { Equipment() } bind ItemContainer::class
                factory(named("bank")) { Inventory() } bind ItemContainer::class
            })
        }
        val koin = GlobalContext.get()
        val xteaManager: XteaManager = get()

        val ticker: WorldTicker = koin.get()
        val db: ArandarDatabaseManager = koin.get()

        xteaManager.load(Path.of("/home/javatar/IdeaProjects/Arandar-Server/data/region_xtea"))
        db.connect()

        ticker.onTick(LoginEvents())
        ticker.onTick(PlayersEvent())
        ticker.onTick(PlayerShuffleEvent())

        ArandarServer().start(InetSocketAddress(43594))

    }

}