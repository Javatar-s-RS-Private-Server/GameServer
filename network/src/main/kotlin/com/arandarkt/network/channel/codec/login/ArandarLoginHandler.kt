package com.arandarkt.network.channel.codec.login

import com.arandarkt.game.api.database.player.PlayerEntityService
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.entity.character.player.PlayerDetails
import com.arandarkt.game.api.koin.inject
import com.arandarkt.game.api.world.PlayerFactory
import com.arandarkt.game.api.world.World
import com.arandarkt.network.channel.NetworkSession.Companion.SESSION_KEY
import com.arandarkt.network.channel.codec.packets.ArandarPacketHandler
import com.arandarkt.network.channel.codec.packets.PacketDecoder
import com.arandarkt.network.channel.codec.packets.PacketEncoder
import com.arandarkt.network.channel.session.packet.PacketSession
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import java.time.LocalDateTime

class ArandarLoginHandler : SimpleChannelInboundHandler<LoginRequest>() {

    val playerService: PlayerEntityService by inject()

    override fun channelRead0(ctx: ChannelHandlerContext, msg: LoginRequest) {
        if(msg.username.isNotBlank() && msg.password.isNotBlank() && msg.response === LoginResponse.SUCCESSFUL) {
            val (validated, details) = validatePlayerLoginRequest(msg, ctx)
            if (validated) {
                val world: World by inject()
                val channel = ctx.channel()
                val session = PacketSession(msg.sessionKey, msg.inCipher, msg.outCipher, channel)
                channel.attr(SESSION_KEY).set(session)
                val player = preparePlayer(details, session)
                ctx.writeAndFlush(LoginRequestResponse(msg.sessionKey, player, msg.response))
                ctx.pipeline().replace("decoder", "decoder", PacketDecoder())
                ctx.pipeline().replace("encoder", "encoder", PacketEncoder())
                ctx.pipeline().replace("handler", "handler", ArandarPacketHandler())
                world.queueLogin(player)
            } else {
                ctx.disconnect()
            }
        } else {
            ctx.disconnect()
        }
    }

    private fun preparePlayer(details: PlayerDetails, session: PacketSession): PlayerCharacter {
        val factory: PlayerFactory by inject()
        return factory.newPlayer(details, session)
    }

    private fun validatePlayerLoginRequest(login: LoginRequest, ctx: ChannelHandlerContext) : Pair<Boolean, PlayerDetails> {
        if(playerService.exists(login.username)) {
            val details = playerService[login.username]
            return (details.username == login.username && details.password == login.password) to details
        } else {
            val details = PlayerDetails(
                login.username,
                login.password,
                "none",
                ctx.channel().remoteAddress().toString(),
                "unknown",
                "none",
                "player",
                LocalDateTime.now()
            )
            playerService.add(details)
            return true to details
        }
    }

    private fun ChannelHandlerContext.setPacketSession(msg: LoginRequest) {
        pipeline().replace("decoder", "decoder", PacketDecoder())
        pipeline().replace("encoder", "encoder", PacketEncoder())
        pipeline().replace("handler", "handler", ArandarPacketHandler())
    }

}