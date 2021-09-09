package com.arandarkt.game.api.entity.components.player.skills

import kotlin.math.floor
import kotlin.math.pow

class VirtualSkill(val id: Int, val maxLevel: Int = 99) {

    var currentLevel: Int = 0
        private set
    var actualLevel: Int = 0
        private set
    var experience: Double = 0.0
        private set

    fun addExperience(experience: Double) : Boolean {
        if((this.experience + experience) >= MAX_EXPERIENCE)
            return false
        val prevLevel = getStaticLevelByExperience(this.experience)
        val newLevel = getStaticLevelByExperience((this.experience + experience))
        this.experience += experience
        if(newLevel > prevLevel) {
            actualLevel = newLevel
        }
        return newLevel > prevLevel
    }

    fun set(actualLevel: Int) {
        this.currentLevel = actualLevel
        this.actualLevel = actualLevel
        experience = getExperienceByLevel(actualLevel)
    }

    fun boost(by : Int) {
        currentLevel += by
    }

    fun restore() {
        if(currentLevel > actualLevel) {
            currentLevel--
        } else if(currentLevel < actualLevel) {
            currentLevel++
        }
    }

    companion object {
        const val MAX_EXPERIENCE = 200000000
        fun getStaticLevelByExperience(exp: Double): Int {
            var points = 0
            var output = 0
            for (lvl in 1..99) {
                points += floor(lvl + 300.0 * 2.0.pow(lvl / 7.0)).toInt()
                output = floor((points / 4).toDouble()).toInt()
                if (output - 1 >= exp) {
                    return lvl
                }
            }
            return 99
        }

        fun getExperienceByLevel(level: Int): Double {
            var points = 0
            var output = 0.0
            for (lvl in 1..level) {
                points += floor(lvl + 300.0 * 2.0.pow(lvl / 7.0)).toInt()
                if (lvl >= level) {
                    return output
                }
                output = floor((points / 4).toDouble())
            }
            return 0.0
        }
    }
}