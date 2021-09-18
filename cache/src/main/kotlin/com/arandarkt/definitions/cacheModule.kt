package com.arandarkt.definitions

import com.arandarkt.definitions.managers.MapFloorManager
import com.arandarkt.definitions.managers.ObjectManager
import com.arandarkt.definitions.managers.ObjectPlacementManager
import org.koin.dsl.module

val cacheModule = module {
    single { MapFloorManager() }
    single { ObjectPlacementManager() }
    single { ObjectManager() }
}