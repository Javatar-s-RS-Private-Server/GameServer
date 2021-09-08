package com.arandarkt.network.channel.session

import com.arandarkt.network.channel.NetworkSession

class HandshakeSession : NetworkSession {

    var nameHash: Int = 0
    var sessionKey: Long = 0

}