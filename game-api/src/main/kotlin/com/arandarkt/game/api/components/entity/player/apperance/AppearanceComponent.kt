package com.arandarkt.game.api.components.entity.player.apperance

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.components.entity.items.DefinitionComponent
import com.arandarkt.game.api.components.entity.player.ItemContainerComponent
import com.arandarkt.game.api.entity.item.GameItem
import com.arandarkt.game.api.entity.item.GameItem.Companion.isNotEmpty

class AppearanceComponent(val player: PlayerCharacter) : Component {

    val bodyParts = IntArray(14) { -1 }
    val animations = intArrayOf(
        STAND_ANIM,
        STAND_TURN_ANIM,
        WALK_ANIM,
        TURN_180_AIM,
        TURN_90_CW,
        TUNR_90_CWW,
        RUN_ANIM
    )
    val icons = intArrayOf(-1, -1)

    var gender = Gender.MALE
        private set

    var npcId = -1

    fun prepareBody(body: Array<BodyPart>) {
        val cons = player.component<ItemContainerComponent>()
        val equipment = cons.equipment

        val chest = equipment.item(SLOT_CHEST)
        val shield = equipment.item(SLOT_SHIELD)
        val legs = equipment.item(SLOT_LEGS)
        val hat = equipment.item(SLOT_HAT)
        val hands = equipment.item(SLOT_HANDS)
        val feet = equipment.item(SLOT_FEET)
        val cape = equipment.item(SLOT_CAPE)
        val amulet = equipment.item(SLOT_AMULET)
        val weapon = equipment.item(SLOT_WEAPON)

        if (hat.isNotEmpty()) {
            setItem(0, hat)
        } else {
            clearBodyPart(0)
        }
        if (cape.isNotEmpty()) {
            setItem(1, cape)
        } else {
            clearBodyPart(1)
        }
        if (amulet.isNotEmpty()) {
            setItem(2, amulet)
        } else {
            clearBodyPart(2)
        }
        if (weapon.isNotEmpty()) {
            setItem(3, weapon)
        } else {
            clearBodyPart(3)
        }
        if (shield.isNotEmpty()) {
            setItem(5, shield)
        } else {
            clearBodyPart(5)
        }
        if (chest.isNotEmpty()) {
            setItem(4, chest)
        } else {
            setClothes(4, body[TORSO].look)
        }

        val chestDef = chest.component<DefinitionComponent>()
        if (chest.isNotEmpty() && chestDef.removeSleeves) {
            clearBodyPart(6)
        } else {
            setClothes(6, body[ARMS].look)
        }
        if (legs.isNotEmpty()) {
            setItem(7, legs)
        } else {
            setClothes(7, body[LEGS].look)
        }
        val hatDef = hat.component<DefinitionComponent>()
        if (hat.isNotEmpty() && hatDef.removeHead) {
            clearBodyPart(8)
        } else {
            setClothes(8, body[HAIR].look)
        }
        if (hands.isNotEmpty()) {
            setItem(9, hands)
        } else {
            setClothes(9, body[WRISTS].look)
        }
        if (feet.isNotEmpty()) {
            setItem(10, feet)
        } else {
            setClothes(10, body[FEET].look)
        }
        if (hat.isNotEmpty() && hatDef.removeBeard) {
            clearBodyPart(11)
        } else {
            setClothes(11, body[FACIAL_HAIR].look)
        }
    }

    fun setItem(part: Int, item: GameItem) {
        bodyParts[part] = item.itemId + 0x200
    }
    fun setClothes(part: Int, clothesId: Int) {
        bodyParts[part] = clothesId + 0x100
    }
    fun clearBodyPart(part: Int) {
        bodyParts[part] = 0
    }

    override suspend fun onTick(currentTick: Long) {}

    companion object {
        /**
         * Represents the index hair apperance is cached at.
         */
        const val HAIR = 0

        /**
         * Represents the index facial hair appearance is cached at.
         */
        const val FACIAL_HAIR = 1

        /**
         * Represents the index torso appearance is cached at.
         */
        const val TORSO = 2

        /**
         * Represents the index arm appearance is cached at.
         */
        const val ARMS = 3

        /**
         * Represents the index wrist appearance is cached at.
         */
        const val WRISTS = 4

        /**
         * Represents the index leg appearance is cached at.
         */
        const val LEGS = 5

        /**
         * Represents the index feet appearance is cached at.
         */
        const val FEET = 6

        /**
         * Represents the index hair color is cached at.
         */
        const val HAIR_COLOR = 0

        /**
         * Represents the index torso color is cached at.
         */
        const val TORSO_COLOR = 1

        /**
         * Represents the index leg color is cached at.
         */
        const val LEG_COLOR = 2

        /**
         * Represents the index feet color is cached at.
         */
        const val FEET_COLOR = 3

        /**
         * Represents the index skin color is cached at.
         */
        const val SKIN_COLOR = 4

        /**
         * The player's stand animation.
         */
        const val STAND_ANIM = 0x328

        /**
         * The player's turn animation for standing.
         */
        const val STAND_TURN_ANIM = 0x337

        /**
         * The player's walk animation.
         */
        val WALK_ANIM = 0x333

        /**
         * The player's turn 180 degrees animation.
         */
        const val TURN_180_AIM = 0x334

        /**
         * The player's turn 90 degrees animation.
         */
        const val TURN_90_CW = 0x335

        /**
         * The player's turn 90 degrees animation.
         */
        const val TUNR_90_CWW = 0x336

        /**
         * The player's run animation.
         */
        const val RUN_ANIM = 0x338

        const val SLOT_HAT = 0
        const val SLOT_CAPE = 1
        const val SLOT_AMULET = 2
        const val SLOT_WEAPON = 3
        const val SLOT_CHEST = 4
        const val SLOT_SHIELD = 5
        const val SLOT_LEGS = 7
        const val SLOT_HANDS = 9
        const val SLOT_FEET = 10
        const val SLOT_RING = 12
        const val SLOT_ARROWS = 13
    }
}