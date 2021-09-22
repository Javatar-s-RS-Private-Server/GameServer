package com.arandarkt.definition

import com.arandarkt.definitions.loaders.ObjectLoader
import com.displee.cache.CacheLibrary
import io.netty.buffer.Unpooled
import org.junit.jupiter.api.Test

class ObjectLoaderTest {

    @Test
    fun `attempt to load 468 objects with 464 loader`() {
        val cache = CacheLibrary.create("/home/javatar/IdeaProjects/ArandarKt/data/cache")

        val loader = ObjectLoader()

        val objects = cache.index(2).archive(6)
        if(objects != null) {
            for (fileId in objects.fileIds()) {
                val data = cache.data(2, 6, fileId)
                if(data != null) {
                    loader.load(Unpooled.wrappedBuffer(data))
                } else {
                    println("Object $fileId does not exist.")
                }
            }
        }

    }

}