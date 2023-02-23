package dev.redicloud.api.paper.utils.parser

import dev.redicloud.api.utils.extension.isUUID
import dev.redicloud.api.utils.extension.toUUID
import dev.redicloud.api.utils.parser.string.IStringTypeParser
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PlayerParser : IStringTypeParser<Player> {

    override fun allowedTypes(): List<Class<out Player>> =
        listOf(Player::class.java)

    override fun parse(value: String): Player {

        if (value.isUUID()) {
            return Bukkit.getPlayer(value.toUUID())!!
        }

        return Bukkit.getPlayer(value)!!
    }
}