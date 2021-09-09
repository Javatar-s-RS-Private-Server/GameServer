package com.arandarkt.game.api.entity.components.player

import com.arandarkt.game.api.entity.Component

class ChatComponent : Component {

    var message: String = ""
    var effects: Int = 0
    var iconId: Int = 0

    var forcedMessage = ""

}