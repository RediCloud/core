package net.dustrean.api.velocity.utils.parser

import com.velocitypowered.api.proxy.Player
import net.dustrean.api.utils.extension.isUUID
import net.dustrean.api.utils.extension.toUUID
import net.dustrean.api.utils.parser.string.IStringTypeParser
import net.dustrean.api.velocity.VelocityCoreAPI

class PlayerParser : IStringTypeParser<Player> {

    override fun allowedTypes(): List<Class<out Player>> =
        listOf(Player::class.java)

    override fun parse(value: String): Player {

        if (value.isUUID()) {
            return VelocityCoreAPI.proxyServer.getPlayer(value.toUUID()).get()
        }

        return VelocityCoreAPI.proxyServer.getPlayer(value).get()
    }
}