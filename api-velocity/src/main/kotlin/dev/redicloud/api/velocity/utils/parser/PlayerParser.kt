package dev.redicloud.api.velocity.utils.parser

import com.velocitypowered.api.proxy.Player
import dev.redicloud.api.utils.extension.isUUID
import dev.redicloud.api.utils.extension.toUUID
import dev.redicloud.api.utils.parser.string.IStringTypeParser
import dev.redicloud.api.velocity.VelocityCoreAPI

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