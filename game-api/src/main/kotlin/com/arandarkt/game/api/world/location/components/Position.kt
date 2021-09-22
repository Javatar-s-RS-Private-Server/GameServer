package com.arandarkt.game.api.world.location.components

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.io.readInt
import com.arandarkt.game.api.io.readUnsignedByte
import com.arandarkt.game.api.io.writeByte
import com.arandarkt.game.api.io.writeInt
import com.arandarkt.game.api.world.map.Direction
import io.guthix.buffer.BitBuf
import kotlin.math.sqrt

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

    fun update(position: Position) {
        this.x = position.x
        this.y = position.y
        this.z = position.z
    }

    fun getRegionId(): Int {
        return x shr 6 shl 8 or (y shr 6)
    }

    fun getRegionX(): Int {
        return x shr 3
    }

    fun getRegionY(): Int {
        return y shr 3
    }

    fun getLocalX(): Int {
        return x - ((x shr 6) shl 6)
    }

    fun getLocalY(): Int {
        return y - ((y shr 6) shl 6)
    }

    fun getSceneX(): Int {
        return x - ((getRegionX() - 6) shl 3)
    }

    fun getSceneY(): Int {
        return y - ((getRegionY() - 6) shl 3)
    }

    fun getSceneX(loc: Position): Int {
        return x - ((loc.getRegionX() - 6) shl 3)
    }

    fun getSceneY(loc: Position): Int {
        return y - ((loc.getRegionY() - 6) shl 3)
    }

    fun withinDistance(other: Position, dist: Int): Boolean {
        if (other.z != z) {
            return false
        }
        val deltaX: Int = other.x - x
        val deltaY: Int = other.y - y
        return deltaX <= dist && deltaX >= -dist && deltaY <= dist && deltaY >= -dist
    }

    fun transform(diffX: Int, diffY: Int, z: Int): Position {
        return Position(x + diffX, y + diffY, this.z + z)
    }

    fun transform(dir: Direction, steps: Int): Position {
        return Position(x + dir.stepX * steps, y + dir.stepY * steps, z)
    }

    fun getDistance(other: Position): Double {
        val xdiff: Int = this.x - other.x
        val ydiff: Int = this.y - other.y
        return sqrt((xdiff * xdiff + ydiff * ydiff).toDouble())
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

        fun getDelta(
            location: Position,
            other: Position
        ): Position {
            return Position(
                other.x - location.x,
                other.y - location.y,
                other.z - location.z
            )
        }
    }
}