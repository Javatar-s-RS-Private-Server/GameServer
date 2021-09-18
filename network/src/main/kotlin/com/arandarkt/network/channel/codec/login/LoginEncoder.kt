package com.arandarkt.network.channel.codec.login

import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.components.entity.player.PlayerMaskComponent
import com.arandarkt.game.api.packets.outgoing.BuildStaticRegion
import com.arandarkt.game.api.world.location.components.Position
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class LoginEncoder : MessageToByteEncoder<LoginRequestResponse>() {
    override fun encode(ctx: ChannelHandlerContext, msg: LoginRequestResponse, out: ByteBuf) {
        println("Writing response " + msg.response)
        out.writeByte(msg.response.opcode())
        try {
            when (msg.response) {
                LoginResponse.SUCCESSFUL -> {
                    val player = msg.player!!
                    val details = player.details
                    println(details.username)
                    if(details.username.lowercase() == "javatar") {
                        out.writeByte(2)
                    } else {
                        out.writeByte(details.role.id)
                    }
                    out.writeByte(0)
                    out.writeShort(player.index)
                    out.writeByte(1)
                    //Write region packet (state 10)
                    out.writeByte(BuildStaticRegion.opcode)
                    val buf = Unpooled.buffer()
                    val flags = player.component<PlayerMaskComponent>()
                    val position = player.component<Position>()
                    flags.lastSceneGraph = position
                    println("X : ${position.x} - ${position.y} - ${position.z}")
                    BuildStaticRegion.encode(
                        buf,
                        BuildStaticRegion(position)
                    )
                    out.writeShort(buf.writerIndex())
                    out.writeBytes(buf)
                }
                LoginResponse.MOVING_WORLD -> {
                    out.writeLong(msg.sessionKey)
                }
                else -> { }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}