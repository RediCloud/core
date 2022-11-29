package net.dustrean.api.paper.utils.parser

import net.dustrean.api.utils.extension.isUUID
import net.dustrean.api.utils.extension.toUUID
import net.dustrean.api.utils.parser.string.IStringTypeParser
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