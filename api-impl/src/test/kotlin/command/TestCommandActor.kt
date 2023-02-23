package command

import dev.redicloud.api.command.ICommandActor
import java.util.*

class TestCommandActor(override val uuid: UUID) : ICommandActor {

    override fun hasPermission(permission: String): Boolean = true

    override fun sendMessage(message: String) {
        println(message)
    }
}