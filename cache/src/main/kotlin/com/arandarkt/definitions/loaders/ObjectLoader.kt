package com.arandarkt.definitions.loaders

import com.arandarkt.definitions.ObjectDefinition
import io.guthix.buffer.readStringCP1252
import io.netty.buffer.ByteBuf
import kotlin.experimental.inv

class ObjectLoader {
    fun load(buffer: ByteBuf): ObjectDefinition {
        val definition = ObjectDefinition()
        while(true) {
            val opcode = buffer.readUnsignedByte().toInt()
            if(opcode == 0)
                break
            definition.decodeOpcode(opcode, buffer)
        }
        return definition
    }

    private fun ObjectDefinition.decodeOpcode(opcode: Int, stream: ByteBuf) {
        if (opcode == 1) {
            val i = stream.readUnsignedByte().toInt()
            if (i.inv() < -1) {
                objectModels = IntArray(i)
                objectTypes = IntArray(i)
                var i_5_ = 0
                while (i.inv() < i_5_.inv()) {
                    objectModels!![i_5_] = stream.readUnsignedShort()
                    objectTypes!![i_5_] = stream.readUnsignedByte().toInt()
                    i_5_++
                }
            }
        } else if (opcode == 2) {
            name = stream.readStringCP1252()
        } else if (opcode == 5) {
            val length = stream.readUnsignedByte().toInt()
            if (length > 0) {
                objectModels = IntArray(length)
                objectTypes = null
                var i_1_ = 0
                while (length > i_1_) {
                    objectModels!![i_1_] = stream.readUnsignedShort()
                    i_1_++
                }
            }
        } else if (opcode == 14) {
            sizeX = stream.readUnsignedByte().toInt()
        } else if (opcode == 15) {
            sizeY = stream.readUnsignedByte().toInt()
        } else if (opcode == 17) {
            interactType = 0
            blocksProjectile = false
        } else if (opcode == 18) {
            blocksProjectile = false
        } else if (opcode == 19) {
            anInt2726 = stream.readUnsignedByte().toInt()
        } else if (opcode == 21) {
            aBoolean2697 = true
        } else if (opcode == 22) {
            nonFlatShading = true
        } else if (opcode == 23) {
            aBoolean2724 = true
        } else if (opcode == 24) {
            animationID = stream.readUnsignedShort()
            if (animationID == 65535)
                animationID = -1
        } else if (opcode == 27) {
            interactType = 1
        } else if (opcode == 28) {
            anInt2750 = stream.readUnsignedByte().toInt()
        } else if (opcode == 29) {
            ambient = stream.readByte().toInt()
        } else if (opcode == 39) {
            contrast = stream.readByte() * 5
        } else if (opcode >= 30 && opcode.inv() > -36) {
            this.actions[-30 + opcode] = stream.readStringCP1252()
        } else if (opcode == 40) {
            val i = stream.readUnsignedByte().toInt()
            recolorToReplace = ShortArray(i)
            recolorToFind = ShortArray(i)
            var i_4_ = 0
            while (i_4_.inv() > i.inv()) {
                recolorToFind!![i_4_] = stream
                    .readUnsignedShort().toShort()
                recolorToReplace!![i_4_] = stream
                    .readUnsignedShort().toShort()
                i_4_++
            }
        } else if(opcode == 41) {
            val textureReplaceCount = stream.readUnsignedByte().toInt()
            textureToFind = ShortArray(textureReplaceCount)
            textureToReplace = ShortArray(textureReplaceCount)
            for (i in 0 until textureReplaceCount) {
                textureToFind!![i] = stream.readUnsignedShort().toShort()
                textureToReplace!![i] = stream.readUnsignedShort().toShort()
            }
        } else if (opcode == 60) {
            anInt2711 = stream.readUnsignedShort()
        } else if (opcode == 62) {
            isRotated = true
        } else if (opcode.inv() == -65) {
            aBoolean2712 = false
        } else if (opcode.inv() == -66) {
            modelSizeX = stream
                .readUnsignedShort()
        } else if (opcode == 66) {
            modelSizeHeight = stream
                .readUnsignedShort()
        } else if (opcode == 67) {
            modelSizeY = stream
                .readUnsignedShort()
        } else if (opcode == 68) {
            mapSceneID = stream
                .readUnsignedShort()
        } else if (opcode == 69) {
            walkingFlag = stream
                .readUnsignedByte().toInt()
        } else if (opcode.inv() == -71) {
            offsetX = stream.readShort().toInt()
        } else if (opcode == 71) {
            offsetHeight = stream.readShort().toInt()
        } else if (opcode == 72) {
            offsetY = stream.readShort().toInt()
        } else if (opcode == 73) {
            aBoolean2730 = true
        } else if (opcode.inv() == -75) {
            isSolid = true
        } else if (opcode == 75) {
            anInt2747 = stream
                .readUnsignedByte().toInt()
        } else if (opcode == 77) {
            varbitID = stream.readUnsignedShort()
            if (varbitID == 65535)
                varbitID = -1
            varpID = stream.readUnsignedShort()
            if (varpID.inv() == -65536)
                varpID = -1
            val i = stream.readUnsignedByte()
            configChangeDest = IntArray(i + 1)
            var i_2_ = 0
            while (i_2_.inv() >= i.inv()) {
                configChangeDest!![i_2_] = stream.readUnsignedShort()
                if (configChangeDest!![i_2_].inv() == -65536)
                    configChangeDest!![i_2_] = -1
                i_2_++
            }
        } else if (opcode == 78) {
            anInt2756 = stream.readUnsignedShort()
            anInt2743 = stream.readUnsignedByte().toInt()
        } else if (opcode == 79) {
            anInt2727 = stream.readUnsignedShort()
            anInt2721 = stream.readUnsignedShort()
            anInt2743 = stream.readUnsignedByte().toInt()
            val i = stream.readUnsignedByte().toInt()
            anIntArray2736 = IntArray(i)
            var i_3_ = 0
            while (i_3_.inv() > i.inv()) {
                anIntArray2736!![i_3_] = stream
                    .readUnsignedShort()
                i_3_++
            }
        } else if(opcode == 81) {
            stream.skipBytes(1)
        }
    }
}