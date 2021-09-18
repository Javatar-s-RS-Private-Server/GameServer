package com.arandarkt.game.api.world.location.components

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.io.readUnsignedByte
import com.arandarkt.game.api.io.readInt
import com.arandarkt.game.api.io.writeByte
import com.arandarkt.game.api.io.writeInt
import io.guthix.buffer.BitBuf

class Position(x: Int = 3222, y: Int = 3217, z: Int = 0) : Component {
    var x: Int = x
        private set
    var y: Int = y
        private set
    var z: Int = z
        private set

    fun update(x: Int, y: Int, z: Int) {
        this.x = x
        this.y = y
        this.z = z
    }

    fun getRegionId(): Int {
        return getRegionX() + (getRegionY() shl 8)
    }

    fun getRegionX(): Int {
        return x shr 3
    }

    fun getRegionY(): Int {
        return y shr 3
    }

    fun getLocalX(): Int {
        return x - (x shr 6 shl 6)
    }

    fun getLocalY(): Int {
        return y - (y shr 6 shl 6)
    }

    fun getSceneX(): Int {
        return x - (getRegionX() - 6 shl 3)
    }

    fun getSceneY(): Int {
        return y - (getRegionY() - 6 shl 3)
    }

    fun getSceneX(loc: Position): Int {
        return x - (loc.getRegionX() - 6 shl 3)
    }

    fun getSceneY(loc: Position): Int {
        return y - (loc.getRegionY() - 6) * 8
    }

    fun withinDistance(other: Position, dist: Int): Boolean {
        if (other.z != z) {
            return false
        }
        val deltaX: Int = other.x - x
        val deltaY: Int = other.y - y
        return deltaX <= dist && deltaX >= -dist && deltaY <= dist && deltaY >= -dist
    }


    fun copy(x: Int = this.x, y: Int = this.y, z: Int = this.z) = Position(x, y, z)


    fun equals(x: Int, y: Int, z: Int): Boolean {
        return equals(Position(x, y, z))
    }

    override fun BitBuf.save() {
        writeInt(x)
        writeInt(y)
        writeByte(z)
    }

    override fun BitBuf.load() {
        x = readInt()
        y = readInt()
        z = readUnsignedByte()
    }

    override fun toString(): String {
        return "[" + getRegionId() + ", " + x + ", " + y + ", " + z + "]"
    }

    override fun hashCode(): Int {
        return z shl 30 or (x shl 15) or y
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    companion object {
        val VOID_LOCATION = Position(-1, -1, -1)
    }
}