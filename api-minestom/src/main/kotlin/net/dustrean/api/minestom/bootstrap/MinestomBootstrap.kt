package net.dustrean.api.minestom.bootstrap

import net.dustrean.api.minestom.MinestomCoreAPI
import net.minestom.server.MinecraftServer

fun main(args: Array<String>) {
    val server = MinecraftServer.init()

    server.start("0.0.0.0", System.getenv("service.bind.port").toInt())

    MinestomCoreAPI.init(server)

    Runtime.getRuntime().addShutdownHook(Thread {
        MinestomCoreAPI.shutdown()
    })
}
