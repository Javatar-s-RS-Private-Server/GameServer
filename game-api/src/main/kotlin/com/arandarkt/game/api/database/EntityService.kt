package com.arandarkt.game.api.database

interface EntityService<T, ID> {


    fun all(): Iterable<T>

    operator fun get(id: ID) : T

    fun add(value: T)

    fun update(value: T)

    fun delete(id: ID)

    fun getOrAdd(value: T) : T

    fun exists(id: ID): Boolean

}