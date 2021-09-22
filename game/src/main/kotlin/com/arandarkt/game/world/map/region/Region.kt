package com.arandarkt.game.world.map.region

import com.arandarkt.game.api.components.entity.objects.ObjectDefinitionComponent
import com.arandarkt.game.api.components.entity.objects.SpawnComponent
import com.arandarkt.game.api.entity.`object`.GameObject
import com.arandarkt.game.api.entity.character.Character
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.world.location.components.Position
import com.arandarkt.game.api.world.map.MapRegion
import java.util.*
import kotlin.collections.HashSet

class Region(override val regionId: Int, override val landscape: Array<Array<BooleanArray>> = emptyArray()) : MapRegion {

    override val players = LinkedList<PlayerCharacter>()
    override val npcs = LinkedList<Character>()
    override val objects = LinkedList<GameObject>()
    override val clippingMasks: Array<Array<IntArray>> = Array(4) { Array(128) { IntArray(128) } }

    override val regionX: Int
        get() = regionId shr 8
    override val regionY: Int
        get() = regionId and 255

    override var isClipped = false
        private set

    override fun addClipping(obj: GameObject) {
        val pos = obj.component<Position>()
        val def = obj.component<ObjectDefinitionComponent>().def
        val spawn = obj.component<SpawnComponent>()
        val x: Int = pos.x
        val y: Int = pos.y
        val height: Int = pos.z
        val xLength: Int
        val yLength: Int
        if (spawn.rotation != 1 && spawn.rotation != 3) {
            xLength = def.sizeX
            yLength = def.sizeY
        } else {
            xLength = def.sizeY
            yLength = def.sizeX
        }
        val type: Int = spawn.type
        if (type == 22) {
            if (def.interactType == 1) {
                addClipping(x, y, height, 0x200000)
            }
        } else if (type in 9..11) {
            if (def.interactType != 0) {
                addClippingForSolidObject(
                    x,
                    y,
                    height,
                    xLength,
                    yLength,
                    def.isSolid
                )
            }
        } else if (type in 0..3) {
            if (def.interactType != 0) {
                addClippingForVariableObject(
                    x,
                    y,
                    height,
                    type,
                    spawn.rotation,
                    def.isSolid
                )
            }
        }
    }

    override fun addClipping(x: Int, y: Int, z: Int, mask: Int) {
        val localX = x - (x shr 7 shl 7)
        val localY = y - (y shr 7 shl 7)
        isClipped = true
        clippingMasks[z][localX][localY] = clippingMasks[z][localX][localY] or mask
    }

    override fun removeClipping(obj: GameObject) {
        val pos = obj.component<Position>()
        val def = obj.component<ObjectDefinitionComponent>().def
        val spawn = obj.component<SpawnComponent>()
        val xLength: Int
        val yLength: Int
        val x: Int = pos.x
        val y: Int = pos.y
        val height: Int = pos.z
        if (spawn.rotation != 1 && spawn.rotation != 3) {
            xLength = def.sizeX
            yLength = def.sizeY
        } else {
            xLength = def.sizeY
            yLength = def.sizeX
        }
        if (spawn.type == 22) {
            if (def.interactType == 1) {
                removeClipping(x, y, height, 0x200000)
            }
        } else if (spawn.type in 9..11) {
            if (def.interactType != 0) {
                removeClippingForSolidObject(
                    x,
                    y,
                    height,
                    xLength,
                    yLength,
                    def.isSolid
                )
            }
        } else if (spawn.type in 0..3) {
            if (def.interactType != 0) {
                removeClippingForVariableObject(
                    x,
                    y,
                    height,
                    spawn.type,
                    spawn.rotation,
                    def.isSolid
                )
            }
        }
    }

    override fun setClipping(x: Int, y: Int, z: Int, mask: Int) {
        val localX = x - (x shr 7 shl 7)
        val localY = y - (y shr 7 shl 7)
        isClipped = true
        clippingMasks[z][localX][localY] = mask
    }

    override fun removeClipping(x: Int, y: Int, z: Int, mask: Int) {
        val localX = x - (x shr 7 shl 7)
        val localY = y - (y shr 7 shl 7)
        clippingMasks[z][localX][localY] = clippingMasks[z][localX][localY] and mask.inv()
    }

    override fun getClippingMask(x: Int, y: Int, z: Int): Int {
        if (!isClipped) {
            println("Region not clipped $regionId")
            return -1
        }
        val localX: Int = x - (x shr 7 shl 7)
        val localY: Int = y - (y shr 7 shl 7)
        val mask = clippingMasks[z][localX][localY]
        if(regionId == 12850) {
            println("X $x $y $z - $mask")
        }
        return clippingMasks[z][localX][localY]
    }

    override fun isPassable(l: Position): Boolean {
        val clippingMask: Int = getClippingMask(l.x, l.y, l.z)
        return if (clippingMask == -1) {
            true
        } else clippingMask < 1
    }

    private fun addClippingForSolidObject(x: Int, y: Int, height: Int, xLength: Int, yLength: Int, flag: Boolean) {
        var clipping = 256
        if (flag) {
            clipping = clipping or 0x20000
        }
        for (i in x until x + xLength) {
            for (i2 in y until y + yLength) {
                addClipping(i, i2, height, clipping)
            }
        }
    }

    private fun removeClippingForSolidObject(x: Int, y: Int, height: Int, xLength: Int, yLength: Int, flag: Boolean) {
        var clipping = 256
        if (flag) {
            clipping = clipping or 0x20000
        }
        for (i in x until x + xLength) {
            for (i2 in y until y + yLength) {
                removeClipping(i, i2, height, clipping)
            }
        }
    }

    private fun addClippingForVariableObject(x: Int, y: Int, z: Int, type: Int, direction: Int, flag: Boolean) {
        if (type == 0) {
            if (direction == 0) {
                addClipping(x, y, z, 128)
                addClipping(x - 1, y, z, 8)
            }
            if (direction == 1) {
                addClipping(x, y, z, 2)
                addClipping(x, y + 1, z, 32)
            }
            if (direction == 2) {
                addClipping(x, y, z, 8)
                addClipping(x + 1, y, z, 128)
            }
            if (direction == 3) {
                addClipping(x, y, z, 32)
                addClipping(x, y - 1, z, 2)
            }
        }
        if (type == 1 || type == 3) {
            if (direction == 0) {
                addClipping(x, y, z, 1)
                addClipping(x - 1, y + 1, z, 16)
            }
            if (direction == 1) {
                addClipping(x, y, z, 4)
                addClipping(x + 1, y + 1, z, 64)
            }
            if (direction == 2) {
                addClipping(x, y, z, 16)
                addClipping(x + 1, y - 1, z, 1)
            }
            if (direction == 3) {
                addClipping(x, y, z, 64)
                addClipping(x - 1, y - 1, z, 4)
            }
        }
        if (type == 2) {
            if (direction == 0) {
                addClipping(x, y, z, 130)
                addClipping(x - 1, y, z, 8)
                addClipping(x, y + 1, z, 32)
            }
            if (direction == 1) {
                addClipping(x, y, z, 10)
                addClipping(x, y + 1, z, 32)
                addClipping(x + 1, y, z, 128)
            }
            if (direction == 2) {
                addClipping(x, y, z, 40)
                addClipping(x + 1, y, z, 128)
                addClipping(x, y - 1, z, 2)
            }
            if (direction == 3) {
                addClipping(x, y, z, 160)
                addClipping(x, y - 1, z, 2)
                addClipping(x - 1, y, z, 8)
            }
        }
        if (flag) {
            if (type == 0) {
                if (direction == 0) {
                    addClipping(x, y, z, 0x10000)
                    addClipping(x - 1, y, z, 4096)
                }
                if (direction == 1) {
                    addClipping(x, y, z, 1024)
                    addClipping(x, y + 1, z, 16384)
                }
                if (direction == 2) {
                    addClipping(x, y, z, 4096)
                    addClipping(x + 1, y, z, 0x10000)
                }
                if (direction == 3) {
                    addClipping(x, y, z, 16384)
                    addClipping(x, y - 1, z, 1024)
                }
            }
            if (type == 1 || type == 3) {
                if (direction == 0) {
                    addClipping(x, y, z, 512)
                    addClipping(x - 1, y + 1, z, 8192)
                }
                if (direction == 1) {
                    addClipping(x, y, z, 2048)
                    addClipping(x + 1, y + 1, z, 32768)
                }
                if (direction == 2) {
                    addClipping(x, y, z, 8192)
                    addClipping(x + 1, y - 1, z, 512)
                }
                if (direction == 3) {
                    addClipping(x, y, z, 32768)
                    addClipping(x - 1, y - 1, z, 2048)
                }
            }
            if (type == 2) {
                if (direction == 0) {
                    addClipping(x, y, z, 0x10400)
                    addClipping(x - 1, y, z, 4096)
                    addClipping(x, y + 1, z, 16384)
                }
                if (direction == 1) {
                    addClipping(x, y, z, 5120)
                    addClipping(x, y + 1, z, 16384)
                    addClipping(x + 1, y, z, 0x10000)
                }
                if (direction == 2) {
                    addClipping(x, y, z, 20480)
                    addClipping(x + 1, y, z, 0x10000)
                    addClipping(x, y - 1, z, 1024)
                }
                if (direction == 3) {
                    addClipping(x, y, z, 0x14000)
                    addClipping(x, y - 1, z, 1024)
                    addClipping(x - 1, y, z, 4096)
                }
            }
        }
    }

    override fun removeClippingForVariableObject(x: Int, y: Int, z: Int, type: Int, direction: Int, flag: Boolean) {
        if (type == 0) {
            if (direction == 0) {
                removeClipping(x, y, z, 128)
                removeClipping(x - 1, y, z, 8)
            }
            if (direction == 1) {
                removeClipping(x, y, z, 2)
                removeClipping(x, 1 + y, z, 32)
            }
            if (direction == 2) {
                removeClipping(x, y, z, 8)
                removeClipping(1 + x, y, z, 128)
            }
            if (direction == 3) {
                removeClipping(x, y, z, 32)
                removeClipping(x, y - 1, z, 2)
            }
        }
        if (type == 1 || type == 3) {
            if (direction == 0) {
                removeClipping(x, y, z, 1)
                removeClipping(x - 1, 1 + y, z, 16)
            }
            if (direction == 1) {
                removeClipping(x, y, z, 4)
                removeClipping(1 + x, y + 1, z, 64)
            }
            if (direction == 2) {
                removeClipping(x, y, z, 16)
                removeClipping(x + 1, -1 + y, z, 1)
            }
            if (direction == 3) {
                removeClipping(x, y, z, 64)
                removeClipping(-1 + x, -1 + y, z, 4)
            }
        }
        if (type == 2) {
            if (direction == 0) {
                removeClipping(x, y, z, 130)
                removeClipping(x - 1, y, z, 8)
                removeClipping(x, 1 + y, z, 32)
            }
            if (direction == 1) {
                removeClipping(x, y, z, 10)
                removeClipping(x, 1 + y, z, 32)
                removeClipping(1 + x, y, z, 128)
            }
            if (direction == 2) {
                removeClipping(x, y, z, 40)
                removeClipping(x + 1, y, z, 128)
                removeClipping(x, -1 + y, z, 2)
            }
            if (direction == 3) {
                removeClipping(x, y, z, 160)
                removeClipping(x, y - 1, z, 2)
                removeClipping(-1 + x, y, z, 8)
            }
        }
        if (flag) {
            if (type == 0) {
                if (direction == 0) {
                    removeClipping(x, y, z, 0x10000)
                    removeClipping(-1 + x, y, z, 4096)
                }
                if (direction == 1) {
                    removeClipping(x, y, z, 1024)
                    removeClipping(x, 1 + y, z, 16384)
                }
                if (direction == 2) {
                    removeClipping(x, y, z, 4096)
                    removeClipping(x + 1, y, z, 0x10000)
                }
                if (direction == 3) {
                    removeClipping(x, y, z, 16384)
                    removeClipping(x, y - 1, z, 1024)
                }
            }
            if (type == 1 || type == 3) {
                if (direction == 0) {
                    removeClipping(x, y, z, 512)
                    removeClipping(-1 + x, 1 + y, z, 8192)
                }
                if (direction == 1) {
                    removeClipping(x, y, z, 2048)
                    removeClipping(1 + x, 1 + y, z, 32768)
                }
                if (direction == 2) {
                    removeClipping(x, y, z, 8192)
                    removeClipping(x + 1, -1 + y, z, 512)
                }
                if (direction == 3) {
                    removeClipping(x, y, z, 32768)
                    removeClipping(x - 1, -1 + y, z, 2048)
                }
            }
            if (type == 2) {
                if (direction == 0) {
                    removeClipping(x, y, z, 0x10400)
                    removeClipping(-1 + x, y, z, 4096)
                    removeClipping(x, y + 1, z, 16384)
                }
                if (direction == 1) {
                    removeClipping(x, y, z, 5120)
                    removeClipping(x, 1 + y, z, 16384)
                    removeClipping(x + 1, y, z, 0x10000)
                }
                if (direction == 2) {
                    removeClipping(x, y, z, 20480)
                    removeClipping(1 + x, y, z, 0x10000)
                    removeClipping(x, -1 + y, z, 1024)
                }
                if (direction == 3) {
                    removeClipping(x, y, z, 0x14000)
                    removeClipping(x, -1 + y, z, 1024)
                    removeClipping(-1 + x, y, z, 4096)
                }
            }
        }
    }


    override fun getClipping(x: Int, y: Int, z: Int, moveTypeX: Int, moveTypeY: Int): Boolean {
        var height = z
        try {
            if (height > 3) {
                height = 0
            }
            val checkX = x + moveTypeX
            val checkY = y + moveTypeY
            if (moveTypeX == -1 && moveTypeY == 0) return getClippingMask(
                x,
                y,
                height
            ) and 0x1280108 == 0
            if (moveTypeX == 1 && moveTypeY == 0) return getClippingMask(x, y, height) and 0x1280180 == 0
            if (moveTypeX == 0 && moveTypeY == -1) return getClippingMask(
                x,
                y,
                height
            ) and 0x1280102 == 0
            if (moveTypeX == 0 && moveTypeY == 1) return getClippingMask(x, y, height) and 0x1280120 == 0
            if (moveTypeX == -1 && moveTypeY == -1) return getClippingMask(
                x,
                y,
                height
            ) and 0x128010E == 0 && getClippingMask(
                checkX - 1,
                checkY,
                height
            ) and 0x1280108 == 0 && getClippingMask(checkX, checkY - 1, height) and 0x1280102 == 0
            if (moveTypeX == 1 && moveTypeY == -1) return getClippingMask(
                x,
                y,
                height
            ) and 0x1280183 == 0 && getClippingMask(
                checkX + 1,
                checkY,
                height
            ) and 0x1280180 == 0 && getClippingMask(checkX, checkY - 1, height) and 0x1280102 == 0
            if (moveTypeX == -1 && moveTypeY == 1) return getClippingMask(
                x,
                y,
                height
            ) and 0x1280138 == 0 && getClippingMask(
                checkX - 1,
                checkY,
                height
            ) and 0x1280108 == 0 && getClippingMask(checkX, checkY + 1, height) and 0x1280120 == 0
            return if (moveTypeX == 1 && moveTypeY == 1) {
                getClippingMask(
                    x,
                    y,
                    height
                ) and 0x12801E0 == 0 && getClippingMask(
                    checkX + 1,
                    checkY,
                    height
                ) and 0x1280180 == 0 && getClippingMask(checkX, checkY + 1, height) and 0x1280120 == 0
            } else false
        } catch (e: Exception) {
        }
        return true
    }

    override fun blockedNorth(loc: Position): Boolean {
        return getClippingMask(loc.x, loc.y + 1, loc.z) and 0x1280120 != 0
    }

    override fun blockedEast(loc: Position): Boolean {
        return getClippingMask(loc.x + 1, loc.y, loc.z) and 0x1280180 != 0
    }

    override fun blockedSouth(loc: Position): Boolean {
        return getClippingMask(loc.x, loc.y - 1, loc.z) and 0x1280102 != 0
    }

    override fun blockedWest(loc: Position): Boolean {
        return getClippingMask(loc.x - 1, loc.y, loc.z) and 0x1280108 != 0
    }

    override fun blockedNorthEast(loc: Position): Boolean {
        return getClippingMask(loc.x + 1, loc.y + 1, loc.z) and 0x12801E0 != 0
    }

    override fun blockedNorthWest(loc: Position): Boolean {
        return getClippingMask(loc.x - 1, loc.y + 1, loc.z) and 0x1280138 != 0
    }

    override fun blockedSouthEast(loc: Position): Boolean {
        return getClippingMask(loc.x + 1, loc.y - 1, loc.z) and 0x1280183 != 0
    }

    override fun blockedSouthWest(loc: Position): Boolean {
        return getClippingMask(loc.x - 1, loc.y - 1, loc.z) and 0x128010E != 0
    }



}