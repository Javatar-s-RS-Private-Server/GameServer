package com.arandarkt.application

import com.arandarkt.game.api.database.ArandarDatabaseManager
import com.arandarkt.game.api.database.player.PlayerEntityService
import com.arandarkt.game.api.entity.collection.EntityLists
import com.arandarkt.game.api.entity.item.GameItemBuilder
import com.arandarkt.game.api.packets.packetModule
import com.arandarkt.game.api.world.Ticker
import com.arandarkt.game.api.world.World
import com.arandarkt.game.api.world.map.IRegionManager
import com.arandarkt.game.entity.collection.EntityCollections
import com.arandarkt.game.entity.item.ItemBuilder
import com.arandarkt.game.world.GameWorld
import com.arandarkt.game.world.PlayerFactory
import com.arandarkt.game.world.WorldTicker
import com.arandarkt.game.world.events.LoginEvents
import com.arandarkt.game.world.events.PlayerShuffleEvent
import com.arandarkt.game.world.events.PlayersEvent
import com.arandarkt.game.world.map.RegionManager
import com.arandarkt.network.ArandarServer
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import io.netty.buffer.Unpooled
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import java.net.InetSocketAddress

object Application {

    @JvmStatic
    fun main(args: Array<String>) {

        startKoin {
            modules(module {
                single { PlayerEntityService() }
                single { ArandarDatabaseManager() }
                single { RegionManager() } bind IRegionManager::class
                single { WorldTicker() } bind Ticker::class
                single { GameWorld() } bind World::class
                single { PlayerFactory() } bind com.arandarkt.game.api.world.PlayerFactory::class
                single { EntityCollections() } bind EntityLists::class
                factory { ItemBuilder() } bind GameItemBuilder::class
            }, packetModule)
        }
        val koin = GlobalContext.get()

        val ticker: WorldTicker = koin.get()
        val db: ArandarDatabaseManager = koin.get()

        db.connect()

        ticker.onTick(LoginEvents())
        ticker.onTick(PlayersEvent())
        ticker.onTick(PlayerShuffleEvent())

        ArandarServer().start(InetSocketAddress(43594))

    }

}