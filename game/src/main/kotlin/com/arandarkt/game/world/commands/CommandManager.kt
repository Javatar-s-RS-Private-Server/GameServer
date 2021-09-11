package com.arandarkt.game.world.commands

import com.arandarkt.game.api.components.Component
import com.arandarkt.game.api.components.ComponentManager
import com.arandarkt.game.api.entity.character.player.PlayerCharacter
import com.arandarkt.game.api.packets.PacketHandler
import com.arandarkt.game.api.packets.incoming.Command
import com.arandarkt.game.api.world.commands.CommandComponent
import com.arandarkt.game.api.world.commands.GameCommandManager
import kotlin.reflect.KClass

class CommandManager(private val player: PlayerCharacter) : GameCommandManager, PacketHandler<Command>, Component {
    override val components: ComponentManager<CommandComponent> = ComponentManager()
    private val commands = mutableMapOf<String, KClass<*>>()
    override fun handlePacket(packet: Command) {
        println("Attempting command ${packet.command}")
        val args = packet.command.split(" ")
        val name = args[0]
        val kclass = commands[name]
        if(kclass != null) {
            components.getComponent(kclass)?.onCommand(player, name, *args.subList(1, args.size).toTypedArray())
        }
    }

    fun addCommand(command : CommandComponent) {
        components.with(command)
        commands[command.name] = command::class
    }
}