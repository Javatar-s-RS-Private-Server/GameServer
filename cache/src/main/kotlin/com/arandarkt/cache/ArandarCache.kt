package com.arandarkt.cache

import com.displee.cache.CacheLibrary

object ArandarCache {

    private lateinit var cacheLibrary: CacheLibrary

    fun cache() : CacheLibrary {
        return if(this::cacheLibrary.isInitialized) {
            cacheLibrary
        } else {
            cacheLibrary = CacheLibrary.create("/home/javatar/IdeaProjects/Arandar-Server/data/cache")
            cacheLibrary
        }
    }

}