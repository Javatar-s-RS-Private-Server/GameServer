package com.arandarkt.game.api.packets.outgoing

import com.arandarkt.game.api.koin.inject
import com.arandarkt.game.api.packets.GamePacketEncoder
import com.arandarkt.game.api.packets.PacketHeader
import com.arandarkt.game.api.world.location.components.PositionComponent
import com.arandarkt.xtea.XteaManager
import io.guthix.buffer.writeShortAdd
import io.netty.buffer.ByteBuf

class BuildStaticRegion(val position: PositionComponent) {
    companion object : GamePacketEncoder<BuildStaticRegion> {
        private val xteaManager: XteaManager by inject()
        override val opcode: Int = 221
        override val header: PacketHeader = PacketHeader.SHORT
        override fun encode(writer: ByteBuf, data: BuildStaticRegion) {
            with(data.position) {
                val chunkX = x shr 3
                val chunkZ = y shr 3
                val lx = (chunkX - (MAX_VIEWPORT shr 4)) shr 3
                val rx = (chunkX + (MAX_VIEWPORT shr 4)) shr 3
                val lz = (chunkZ - (MAX_VIEWPORT shr 4)) shr 3
                val rz = (chunkZ + (MAX_VIEWPORT shr 4)) shr 3
                writer.writeShort(getRegionX())
                for (regionX in lx..rx) {
                    for (regionY in lz..rz) {
                        /*val regionId = regionY + (regioSmnX shl 8)
                        val regionKey = xteaManager.xteas[regionId]
                        println(regionId)
                        if(regionKey != null) {
                            val keys = regionKey.key
                            writer.writeInt(keys[0])
                            writer.writeInt(keys[1])
                            writer.writeInt(keys[2])
                            writer.writeInt(keys[3])
                        } else {
                            repeat(4) { writer.writeInt(0) }
                        }*/
                        repeat(4) { writer.writeInt(0) }
                    }
                }
                writer.writeShort(getRegionY())
                writer.writeShort(getSceneX())
                writer.writeByte(z)
                writer.writeShort(getSceneY())
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
}