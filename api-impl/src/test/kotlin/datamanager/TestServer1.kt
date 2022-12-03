package datamanager

import com.fasterxml.jackson.databind.ObjectMapper
import datamanager.TestCoreAPI
import datamanager.TestPlayer
import java.util.*

fun main() {

    val coreAPI = TestCoreAPI(UUID.fromString("b04e8b0d-d274-4069-b7a6-fb26d7f17357"))


    val player1UUID = UUID.fromString("8f883514-5d2a-4a60-adf3-5838bed0ec64")
    coreAPI.playerManager.existsObject(player1UUID).whenComplete { exists, throwable ->
        if (throwable != null) {
            throwable.printStackTrace()
            return@whenComplete
        }
        if(exists){
            coreAPI.playerManager.getObject(player1UUID).whenComplete { player, throwable1 ->
                if (throwable1 != null) {
                    throwable1.printStackTrace()
                    return@whenComplete
                }
                println("Get player $player1UUID")
                println(player)
            }
            return@whenComplete
        }
        val player1 = TestPlayer(player1UUID)
        coreAPI.playerManager.createObject(player1).whenComplete { created, throwable2 ->
            if (throwable2 != null) {
                throwable2.printStackTrace()
                return@whenComplete
            }
            println("Created player $player1UUID")
        }
    }

    Scanner(System.`in`).nextLine()
    coreAPI.playerManager.getObject(player1UUID).whenComplete { player, throwable1 ->
        if (throwable1 != null) {
            throwable1.printStackTrace()
            return@whenComplete
        }
        println("Get player $player1UUID")
        println(ObjectMapper().writeValueAsString(player))
    }

    Scanner(System.`in`).nextLine()
    println("Init shutdown")
    coreAPI.shutdown()
    println("Shutdown complete")
}