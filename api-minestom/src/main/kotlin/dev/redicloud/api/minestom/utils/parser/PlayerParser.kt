package dev.redicloud.api.minestom.utils.parser

import dev.redicloud.api.utils.extension.isUUID
import dev.redicloud.api.utils.extension.toUUID
import dev.redicloud.api.utils.parser.string.IStringTypeParser
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player

class PlayerParser : IStringTypeParser<Player> {

    override fun allowedTypes(): List<Class<out Player>> =
        listOf(Player::class.java)

    override fun parse(value: String): Player {

        if (value.isUUID()) {
            return MinecraftServer.getConnectionManager().getPlayer(value.toUUID())!!
        }

        return MinecraftServer.getConnectionManager().getPlayer(value)!!
    }
}