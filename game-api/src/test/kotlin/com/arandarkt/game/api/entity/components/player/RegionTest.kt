package com.arandarkt.game.api.entity.components.player

import org.junit.jupiter.api.Test

class RegionTest {

    @Test
    fun `region and region id generation`() {
        val x = 3222
        val y = 3217
        val z = 0

        val chunkX = x shr 3
        val chunkZ = y shr 3

        val lx = (chunkX - (MAX_VIEWPORT shr 4)) shr 3
        val rx = (chunkX + (MAX_VIEWPORT shr 4)) shr 3
        val lz = (chunkZ - (MAX_VIEWPORT shr 4)) shr 3
        val rz = (chunkZ + (MAX_VIEWPORT shr 4)) shr 3

        println("RX: $rx RY: $rz")

        for (x in lx..rx) {
            for (z in lz..rz) {
                val regionId = z + (x shl 8)
                println(regionId)
            }
        }

    }

    /**
     * The size of a chunk, in tiles.
     */
    val CHUNK_SIZE = 8

    /**
     * The amount of chunks in a region.
     */
    val CHUNKS_PER_REGION = 13

    /**
     * The size of the viewport a [Player] can
     * 'see' at a time, in tiles.
     */
    val MAX_VIEWPORT = CHUNK_SIZE * CHUNKS_PER_REGION

}