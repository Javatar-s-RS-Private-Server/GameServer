package com.arandarkt.game.api.entity.components.player.apperance

enum class Gender(val appearanceCache: Array<BodyPart>) {
    MALE(
        arrayOf(
            BodyPart(0),
            BodyPart(10),
            BodyPart(18),
            BodyPart(26),
            BodyPart(33),
            BodyPart(36),
            BodyPart(42)
        )
    ),
    FEMALE(arrayOf(BodyPart(45), BodyPart(1000), BodyPart(56), BodyPart(61), BodyPart(68), BodyPart(70), BodyPart(79)));

    fun generateBody(): Array<BodyPart> {
        return Array(appearanceCache.size) { BodyPart(appearanceCache[it].look) }
    }

    fun toByte(): Int {
        return (if (this == MALE) 0 else 1)
    }

    fun asByte(value: Byte): Gender {
        return if (value.toInt() == 0) MALE else FEMALE
    }
}