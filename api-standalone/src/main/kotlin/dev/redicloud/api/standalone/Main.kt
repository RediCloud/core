package dev.redicloud.api.standalone

import dev.redicloud.api.module.ModuleState
import dev.redicloud.api.standalone.impl.StandaloneCoreAPI
import dev.redicloud.api.utils.ExceptionHandler
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    ExceptionHandler.service = "StandaloneCoreAPI"
    println("Starting API Standalone...")
    val coreAPI = StandaloneCoreAPI
    Runtime.getRuntime().addShutdownHook(Thread {
        coreAPI.shutdown()
        println("API Standalone stopped!")
    })
    coreAPI.initialized()
    println("API Standalone started!")
    if (System.getProperty("redicloud.service") == null)
    while (true) {
        try {
            print("\nAvailable commands: list, reload, stop\n> ")
            val command = readlnOrNull()!!
            when (command.lowercase()) {
                "list" -> println(ModuleState.values().joinToString(separator = "\n") {
                    "${it.name}:    ${coreAPI.moduleManager.getModules(it).joinToString()}"
                })

                "reload" -> {
                    println("Reloading modules...")
                    coreAPI.moduleManager.reloadModules()
                    println("Modules reloaded!")
                }

                "stop" -> {
                    println("Stopping API Standalone...")
                    exitProcess(0)
                }

                else -> println("Unknown command!")
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
}
