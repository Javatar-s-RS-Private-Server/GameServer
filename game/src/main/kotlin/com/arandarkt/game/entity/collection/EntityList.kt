package com.arandarkt.game.entity.collection

import com.arandarkt.game.api.entity.Entity
import com.arandarkt.game.api.entity.collection.EntityList
import java.util.*
import java.util.function.IntFunction
import java.util.stream.Stream
import java.util.stream.StreamSupport

abstract class EntityList<T : Entity> : EntityList<T> {

    abstract val entities: Array<T?>

    fun nextIndex(): Int {
        for (i in 1 until entities.size) {
            if (entities[i] == null) {
                return i
            }
        }
        return -1
    }

    override fun contains(element: T?): Boolean {
        return entities.contains(element)
    }

    override fun containsAll(elements: Collection<T?>): Boolean {
        for (element in elements) {
            if(element !in entities) {
                return false
            }
        }
        return true
    }

    override fun isEmpty(): Boolean {
        for (entity in entities) {
            if(entity != null)
                return true
        }
        return false
    }

    override fun iterator(): Iterator<T?> {
        return entities.iterator()
    }

    override fun parallelStream(): Stream<T?> {
        return StreamSupport.stream(spliterator(), true)
    }

    override fun spliterator(): Spliterator<T?> {
        return Spliterators.spliterator(entities, 0)
    }

    override fun stream(): Stream<T?> {
        return StreamSupport.stream(spliterator(), false)
    }

    @Deprecated("Don't use this lol", level = DeprecationLevel.HIDDEN)
    override fun <T : Any?> toArray(generator: IntFunction<Array<T>>?): Array<T> {
        return super.toArray(generator)
    }
}