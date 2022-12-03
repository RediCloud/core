package datamanager

import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*

fun main() {

    val coreAPI = TestCoreAPI(UUID.fromString("1ec98654-f2ac-4a97-84b8-7d8e00030f4e"))


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
                println(ObjectMapper().writeValueAsString(player))
                val coins = (0..100).random()
                println("Set coins to $coins")
                player.coins = coins
                player.update()
                println("Updated player $player1UUID")
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

    Scanner(System.`in`).nextLine()
    println("Init shutdown")
    coreAPI.shutdown()
    println("Shutdown complete")
}