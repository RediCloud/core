package net.dustrean.api.minestom

import net.dustrean.api.cloud.CloudCoreAPI
import net.dustrean.api.minestom.utils.parser.PlayerParser
import net.dustrean.api.utils.parser.string.StringParser
import net.minestom.server.MinecraftServer

object MinestomCoreAPI : CloudCoreAPI() {

    // instace of MinecraftServer will be initialized by Refections
    val server: MinecraftServer = null!!
    val isInitialized: Boolean = false

    fun init(server: MinecraftServer) {
        MinestomCoreAPI::class.java.getDeclaredField("server").apply {
            isAccessible = true
            set(this@MinestomCoreAPI, server)
            isAccessible = false
        }
        MinestomCoreAPI::class.java.getDeclaredField("isInitialized").apply {
            isAccessible = true
            set(this@MinestomCoreAPI, true)
            isAccessible = false
        }

        StringParser.customTypeParsers.add(PlayerParser())
    }

}