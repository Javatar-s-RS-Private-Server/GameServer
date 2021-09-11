package com.arandarkt.xtea

import java.nio.file.Path

fun interface XteaLoader {

    fun load(path: Path) : List<RegionKey>

}