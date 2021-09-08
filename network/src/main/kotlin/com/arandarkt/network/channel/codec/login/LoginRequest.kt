package com.arandarkt.network.channel.codec.login

import com.arandarkt.network.cipher.IsaacCipher

data class LoginRequest(
    val username: String,
    val password: String,
    val sessionKey: Long,
    val response: LoginResponse,
    val inCipher: IsaacCipher = IsaacCipher(intArrayOf()),
    val outCipher: IsaacCipher = IsaacCipher(intArrayOf())
)