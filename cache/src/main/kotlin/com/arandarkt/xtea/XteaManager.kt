package com.arandarkt.xtea

import java.nio.file.Path

class XteaManager(private val loader: XteaLoader) {

    val xteas = mutableMapOf<Int, RegionKey>()

    fun load(path: Path) {
        val keys = loader.load(path)
        xteas.clear()
        xteas.putAll(keys.associateBy { it.mapsquare })
        println("Loaded ${xteas.size} Region Keys.")
    }

}