package net.dustrean.api.standalone

import net.dustrean.api.module.ModuleState
import net.dustrean.api.standalone.impl.StandaloneCoreAPI
import net.dustrean.api.utils.ExceptionHandler
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    ExceptionHandler.service = "StandaloneCoreAPI"
    println("Starting API Standalone...")
    val coreAPI = StandaloneCoreAPI
    println("API Standalone started!")
    while (true) {
        try {
            print("\nAvailable commands: list, reload, stop\n> ")
            val command = readlnOrNull()!!
            when (command.lowercase()) {
                "list" -> println(ModuleState.values().joinToString(separator = "\n") {
                    "${it.name}:    ${coreAPI.getModuleHandler().getModules(it).joinToString()}"
                })

                "reload" -> {
                    println("Reloading modules...")
                    coreAPI.getModuleHandler().reloadModules()
                    println("Modules reloaded!")
                }

                "stop" -> {
                    println("Stopping API Standalone...")
                    coreAPI.shutdown()
                    println("API Standalone stopped!")
                    exitProcess(0)
                }

                else -> println("Unknown command!")
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
}