package com.arandarkt.game.api.entity.components.items

import com.arandarkt.game.api.entity.Component
import com.arandarkt.game.api.io.readInt
import com.arandarkt.game.api.io.writeInt
import io.guthix.buffer.BitBuf
import io.guthix.buffer.toBitMode
import io.netty.buffer.ByteBuf
import kotlin.reflect.KProperty

class IdentificationComponent(private var itemId: Int, private var amount: Int) : Component {

    operator fun getValue(ref: Any?, property: KProperty<*>): Int {
        if (property.name.lowercase() == "itemid") {
            return itemId
        } else if (property.name.lowercase() == "amount") {
            return amount
        }
        return 0
    }

    override fun BitBuf.save() {
        writeBoolean(itemId != -1)
        if(itemId != -1) {
            writeInt(itemId)
            writeInt(amount)
        }
    }

    override fun BitBuf.load() {
        if (readBoolean()) {
            itemId = readInt()
            amount = readInt()
        }
    }

    override suspend fun onTick(currentTick: Long) {}
}