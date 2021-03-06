package com.arandarkt.game.api.components.entity.flags.player

import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.components.entity.flags.FlagComponent
import com.arandarkt.game.api.components.entity.player.skills.SkillsComponent
import com.arandarkt.game.api.components.entity.player.apperance.AppearanceComponent
import com.arandarkt.game.api.components.entity.player.apperance.AppearanceComponent.Companion.FEET
import com.arandarkt.game.api.components.entity.player.apperance.AppearanceComponent.Companion.HAIR
import com.arandarkt.game.api.components.entity.player.apperance.AppearanceComponent.Companion.LEGS
import com.arandarkt.game.api.components.entity.player.apperance.AppearanceComponent.Companion.SKIN_COLOR
import com.arandarkt.game.api.components.entity.player.apperance.AppearanceComponent.Companion.TORSO
import com.arandarkt.game.api.primitives.stringToLong
import io.guthix.buffer.writeByteAdd
import io.guthix.buffer.writeBytesReversed
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled

class AppearanceFlag(val player: PlayerCharacter) : FlagComponent {
    override val flagId: Int = 0x40

    override fun ByteBuf.writeFlag() {
        val app = player.component<AppearanceComponent>()
        val skills = player.component<SkillsComponent>()
        val body = app.gender.generateBody()
        app.prepareBody(body)

        val buf = with(Unpooled.buffer()) {
            writeByte(app.gender.toByte())
            writeByte(app.icons[0]) //skull icon
            writeByte(app.icons[1]) //head icon

            if (app.npcId == -1) {
                val parts = app.bodyParts
                for (i in 0..11) {
                    val value = parts[i]
                    if (value == 0) {
                        writeByte(0)
                    } else {
                        writeShort(value)
                    }
                    println("Index $i - $value pos ${writerIndex()}")
                }
            } else {
                writeShort(-1)
                writeShort(app.npcId)
            }

            println("Written " + writerIndex())

            arrayOf(body[HAIR], body[TORSO], body[LEGS], body[FEET], body[SKIN_COLOR])
                .forEach { writeByte(it.color) }

            writeShort(app.animations[0])
            writeShort(app.animations[1])
            writeShort(app.animations[2])
            writeShort(app.animations[3])
            writeShort(app.animations[5])
            writeShort(app.animations[4])
            writeShort(app.animations[6])

            writeLong(stringToLong(player.details.username))
            writeByte(skills.combatLevel)
            writeShort(skills.skillLevel)
        }

        println("Writing app flag with size " + buf.writerIndex())
        writeByte(buf.writerIndex())
        writeBytes(buf)
    }
}