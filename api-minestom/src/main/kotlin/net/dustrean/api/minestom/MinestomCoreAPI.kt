package net.dustrean.api.minestom

import net.dustrean.api.cloud.CloudCoreAPI
import net.dustrean.api.minestom.utils.parser.PlayerParser
import net.dustrean.api.utils.parser.string.StringParser
import net.minestom.server.MinecraftServer

object MinestomCoreAPI : CloudCoreAPI() {

    fun init() {
        StringParser.customTypeParsers.add(PlayerParser())
    }

}