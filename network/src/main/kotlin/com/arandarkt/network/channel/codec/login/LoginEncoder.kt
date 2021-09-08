package com.arandarkt.network.channel.codec.login

import com.arandarkt.game.api.entity.component
import com.arandarkt.game.api.entity.components.player.PlayerMaskComponent
import com.arandarkt.game.api.packets.outgoing.BuildStaticRegion
import com.arandarkt.game.api.world.location.components.PositionComponent
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class LoginEncoder : MessageToByteEncoder<LoginRequestResponse>() {
    override fun encode(ctx: ChannelHandlerContext, msg: LoginRequestResponse, out: ByteBuf) {
        val player = msg.player
        val details = player.details
        out.writeByte(msg.response.opcode())
        when (msg.response) {
            LoginResponse.SUCCESSFUL -> {
                out.writeByte(details.role.id)
                out.writeByte(0)
                out.writeShort(player.index)
                out.writeByte(1)
                //Write region packet (state 10)
                out.writeByte(BuildStaticRegion.opcode)
                val buf = Unpooled.buffer()
                val flags = player.component<PlayerMaskComponent>()
                val position = player.component<PositionComponent>()
                flags.lastSceneGraph = position
                BuildStaticRegion.encode(
                    buf,
                    BuildStaticRegion(
                        position.getRegionX(),
                        position.getRegionY(),
                        position.getSceneX(),
                        position.getSceneY(),
                        position.z
                    )
                )
                out.writeShort(buf.writerIndex())
                out.writeBytes(buf)
            }
            LoginResponse.MOVING_WORLD -> {
                out.writeLong(msg.sessionKey)
            }
            else -> {
            }
        }
    }
}