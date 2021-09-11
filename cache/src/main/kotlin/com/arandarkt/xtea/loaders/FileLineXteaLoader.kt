package com.arandarkt.xtea.loaders

import com.arandarkt.xtea.RegionKey
import com.arandarkt.xtea.XteaLoader
import java.nio.file.Path

class FileLineXteaLoader : XteaLoader {
    override fun load(path: Path): List<RegionKey> {
        val files = path.toFile().listFiles()
        if(files != null && files.isNotEmpty()) {
            val regionKeys = mutableListOf<RegionKey>()
            for (file in files) {
                val regionId = file.nameWithoutExtension.toInt()
                val keys = file.readLines()
                val regionKey = RegionKey(regionId, keys.map { it.toInt() }.toIntArray())
                regionKeys.add(regionKey)
            }
            return regionKeys
        }
        return emptyList()
    }
}