package com.arandarkt.game.entity.collection.character.impl

import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.entity.collection.character.CharacterList
import java.util.concurrent.ThreadLocalRandom

class PlayerList(override val size: Int = 2000) : CharacterList<PlayerCharacter>() {

    override val entities: Array<PlayerCharacter?> = Array(size) { null }

    private val indices = mutableSetOf<Int>()

    @Synchronized
    override fun addEntity(entity: PlayerCharacter): Int {
        if(!contains(entity)) {
            val index = nextIndex()
            indices.add(index)
            entities[index] = entity
            entity.index = index
        }
        return -1
    }

    @Synchronized
    override fun removeEntity(entity: PlayerCharacter): Boolean {
        if(entities.contains(entity)) {
            indices.remove(entity.index)
            entities[entity.index] = null
            entity.index = -1
            return true
        }
        return false
    }

    override fun shuffle() {
        val shuffled = with(indices.toIntArray()) {
            for (i in lastIndex downTo 1) {
                val j = ThreadLocalRandom.current().nextInt(i + 1)
                val index = this[j]
                val copyIndex = this[i]

                val indexPlayer = entities[index]
                val copyPlayer = entities[copyIndex]

                if (indexPlayer != null && copyPlayer != null && indexPlayer.shouldShuffle() && copyPlayer.shouldShuffle()) {
                    this[i] = index
                    this[j] = copyIndex
                }
            }
            this
        }
        indices.clear()
        indices.addAll(shuffled.toList())
    }

    override fun iterator(): Iterator<PlayerCharacter?> {
        return object : Iterator<PlayerCharacter?> {

            private val iter = indices.iterator()

            override fun hasNext(): Boolean {
                return iter.hasNext()
            }

            override fun next(): PlayerCharacter? {
                return entities[iter.next()]
            }

        }
    }

}