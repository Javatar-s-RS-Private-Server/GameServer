package com.arandarkt.game.api.components.entity.player

import com.arandarkt.game.api.components.Component

class ChatComponent : Component {

    var message: String = ""
    var effects: Int = 0
    var iconId: Int = 0

    var forcedMessage = ""

}