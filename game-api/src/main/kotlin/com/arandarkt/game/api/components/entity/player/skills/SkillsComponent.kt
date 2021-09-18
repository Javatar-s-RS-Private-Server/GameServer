package com.arandarkt.game.api.components.entity.player.skills

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.io.*
import io.guthix.buffer.BitBuf
import java.util.*

class SkillsComponent : Component {

    var combatLevel: Int = 3

    val skills = EnumMap<Skill, VirtualSkill>(Skill::class.java)

    val skillLevel: Int
        get() = 0

    fun getSkill(skill: Skill) : VirtualSkill {
        return skills[skill]!!
    }

    override fun BitBuf.save() {
        writeByte(combatLevel)
        writeBits(skills.size, 5)
        for (skill in skills.values) {
            writeBits(skill.id, 5)
            writeDouble(skill.experience)
            writeByte(skill.maxLevel)
            writeBoolean((skill.currentLevel - skill.actualLevel) > 0)
            if((skill.currentLevel - skill.actualLevel) > 0) {
                writeByte(skill.currentLevel)
            }
        }
    }

    override fun BitBuf.load() {
        combatLevel = readUnsignedByte()
        val size = readBits(5)
        repeat(size) {
            val id = readBits(5)
            val experience = readDouble()
            val maxLevel = readUnsignedByte()
            val boosted = readBoolean()
            val skill = VirtualSkill(id, maxLevel)
            skill.addExperience(experience)
            if(boosted) {
                skill.boost(readUnsignedByte())
            }
            skills[Skill.skills[id]] = skill
        }
    }
}