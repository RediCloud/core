package dev.redicloud.api.velocity.command.impl

import kotlinx.coroutines.runBlocking
import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.command.Command
import dev.redicloud.api.command.ICommandActor
import dev.redicloud.api.command.annotations.CommandArgument
import dev.redicloud.api.command.annotations.CommandSubPath
import dev.redicloud.api.utils.crypt.UpdatableBCrypt
import dev.redicloud.api.velocity.VelocityCoreAPI
import dev.redicloud.api.velocity.config.PlayerAuthConfig

class ChangePasswordCommand : Command(
    commandName = "changepassword",
    commandDescription = "Change your password",
    commandAliases = arrayOf("cp", "changepw", "cpw")
) {


    private var authConfig: PlayerAuthConfig

    init {
        runBlocking {
            authConfig = ICoreAPI.INSTANCE.configManager.getConfig("player-authentication", PlayerAuthConfig::class.java)
        }
    }

    @CommandSubPath
    fun handle(
        actor: ICommandActor,
        @CommandArgument("<old-password>") oldPassword: String,
        @CommandArgument("<new-password>") newPassword: String
    ) = runBlocking {
        if (!authConfig.crackAllowed) {
            actor.sendMessage("§cCrack mode is currently disabled!")
            return@runBlocking
        }
        val proxyPlayer = VelocityCoreAPI.proxyServer.getPlayer(actor.uuid).get()
        if (proxyPlayer.isOnlineMode) {
            actor.sendMessage("§cYou are a premium player!")
            return@runBlocking
        }
        val player = VelocityCoreAPI.playerManager.getPlayerByUUID(actor.uuid)
        if (player == null) {
            actor.sendMessage("§cYou are not registered!")
            return@runBlocking
        }
        if (player.authentication.crypt == null) {
            player.authentication.crypt = UpdatableBCrypt(authConfig.passwordRounds)
        }
        if (!player.authentication.crypt!!.verifyHash(oldPassword, player.authentication.passwordHash)) {
            actor.sendMessage("§cWrong password!")
            return@runBlocking
        }
        player.authentication.passwordHash = player.authentication.crypt!!.hash(newPassword)
        player.update()

        actor.sendMessage("§aPassword changed!")
    }

}
