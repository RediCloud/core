package command

import net.dustrean.api.command.ICommandPlayer
import java.util.*

class TestCommandPlayer(override val uuid: UUID) : ICommandPlayer {

    override fun hasPermission(permission: String): Boolean = true

    override fun sendMessage(message: String) {
        println(message)
    }
}