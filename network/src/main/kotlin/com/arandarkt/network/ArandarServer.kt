package com.arandarkt.network

import com.arandarkt.network.channel.ArandarChannelInitializer
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import java.net.InetSocketAddress

class ArandarServer {

    val bootstrap = ServerBootstrap()
    val eventGroup = NioEventLoopGroup { it ->
        Thread(it).also { it.isDaemon = true }
    }
    val bossGroup = NioEventLoopGroup { it ->
        Thread(it).also { it.isDaemon = true }
    }

    fun start(address: InetSocketAddress) {
        bootstrap.group(eventGroup, bossGroup)
            .channel(NioServerSocketChannel::class.java)
            .childHandler(ArandarChannelInitializer())
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.SO_BACKLOG, 128)

        val future = bootstrap.bind(address).sync()
        future.channel().closeFuture().sync()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            ArandarServer().start(InetSocketAddress(43594))

        }
    }

}