package com.arandarkt.network.channel.codec.login

import com.arandarkt.game.api.koin.inject
import com.arandarkt.network.channel.NetworkSession.Companion.loginSession
import com.arandarkt.network.channel.codec.login.LoginResponse.Companion.toLoginRequest
import com.arandarkt.game.api.primitives.longToString
import com.arandarkt.network.cipher.IsaacCipher
import com.displee.cache.CacheLibrary
import io.guthix.buffer.readString0CP1252
import io.guthix.buffer.readStringCESU8
import io.guthix.buffer.readStringCP1252
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

class LoginDecoder : ByteToMessageDecoder() {

    private val cache: CacheLibrary by inject()

    override fun decode(ctx: ChannelHandlerContext, buffer: ByteBuf, out: MutableList<Any>) {

        val session = ctx.channel().loginSession
        val opcode = buffer.readUnsignedByte()
        val loginSize: Int = buffer.readUnsignedByte().toInt()
        var loginEncryptSize = loginSize - 93

        val revision = buffer.readInt()
        if (revision != 468) {
            out.add(LoginResponse.UPDATED.toLoginRequest(session.sessionKey))
            return
        }

        val lowMemory = buffer.readUnsignedByte().toInt() == 1

        buffer.skipBytes(24)

        for (i in 0 until cache.indices().size) {
            val crc = cache.index(i).crc
            val clientCRC = buffer.readInt()
            if (crc != clientCRC) {
                buffer.readerIndex(buffer.writerIndex())
                out.add(LoginResponse.UPDATED.toLoginRequest(session.sessionKey))
                return
            }
        }

        loginEncryptSize--

        val reportedSize = buffer.readUnsignedByte().toInt()
        if (reportedSize != loginEncryptSize) {
            out.add(LoginResponse.COULD_NOT_LOGIN.toLoginRequest(session.sessionKey))
            return
        }

        val secureId = buffer.readUnsignedByte().toInt()
        println(secureId)
        if (secureId != 10) {
            out.add(LoginResponse.COULD_NOT_LOGIN.toLoginRequest(session.sessionKey))
            return
        }

        val clientKey = buffer.readLong()

        val reportedServerKey = buffer.readLong()

        if (reportedServerKey != session.sessionKey) {
            println("Server key mismatch: " + reportedServerKey + ", " + session.sessionKey)
            buffer.readerIndex(buffer.writerIndex())
            out.add(LoginResponse.COULD_NOT_LOGIN.toLoginRequest(session.sessionKey))
            return
        }

        val isaacSeed = IntArray(4)
        isaacSeed[0] = (clientKey shr 32).toInt()
        isaacSeed[1] = clientKey.toInt()
        isaacSeed[2] = (reportedServerKey shr 32).toInt()
        isaacSeed[3] = reportedServerKey.toInt()

        val inCipher = IsaacCipher(isaacSeed)
        val copy = isaacSeed.copyOf()
        for (i in 0..3) {
            copy[i] += 50
        }
        val outCipher = IsaacCipher(copy)

        try {
            val username: String = longToString(buffer.readLong())
            val password: String = buffer.readStringCP1252()

            println("${buffer.readerIndex()} - ${buffer.writerIndex()}")

            println(username)
            println(password)

            out.add(
                LoginRequest(
                    username,
                    password,
                    session.sessionKey,
                    LoginResponse.SUCCESSFUL,
                    inCipher,
                    outCipher
                )
            )

        } catch (e: Exception) {
            e.printStackTrace()
            out.add(LoginResponse.ERROR_LOADING_PROFILE.toLoginRequest(session.sessionKey))
        }

    }

    private fun formatMacAddress(macSize: Int, macAddressBytes: ByteArray): String {
        val sb = StringBuilder()
        for (i in 0 until macSize) {
            sb.append(String.format("%02X%s", macAddressBytes[i], if (i < macAddressBytes.size - 1) "-" else ""))
        }
        return sb.toString()
    }
}