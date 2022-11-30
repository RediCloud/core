package command

import net.dustrean.api.command.CommandActor
import java.util.*

class TestCommandActor(override val uuid: UUID) : CommandActor {

    override fun hasPermission(permission: String): Boolean = true

    override fun sendMessage(message: String) {
        println(message)
    }
}