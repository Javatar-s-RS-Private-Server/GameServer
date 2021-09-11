package com.arandarkt.xtea.loaders

import com.arandarkt.xtea.RegionKey
import com.arandarkt.xtea.XteaLoader
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.FileReader
import java.nio.file.Path

class XteaJsonLoader : XteaLoader {

    private val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()

    override fun load(path: Path): List<RegionKey> {
        return this.gson.fromJson<List<RegionKey>>(
            FileReader(path.toFile()),
            object : TypeToken<List<RegionKey>>() {}.rawType
        )
    }
}