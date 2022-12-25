package net.dustrean.api.velocity.command.impl

import kotlinx.coroutines.runBlocking
import net.dustrean.api.ICoreAPI
import net.dustrean.api.command.Command
import net.dustrean.api.command.ICommandActor
import net.dustrean.api.command.annotations.CommandArgument
import net.dustrean.api.command.annotations.CommandSubPath
import net.dustrean.api.utils.crypt.UpdatableBCrypt
import net.dustrean.api.velocity.VelocityCoreAPI
import net.dustrean.api.velocity.config.PlayerAuthConfig

class ChangePasswordCommand : Command(
    commandName = "changepassword",
    commandDescription = "Change your password",
    commandAliases = arrayOf("cp", "changepw", "cpw")
) {


    private var authConfig: PlayerAuthConfig

    init {
        runBlocking {
            authConfig = ICoreAPI.INSTANCE.getConfigManager().getConfig("player-authentication")
        }
    }

    @CommandSubPath
    fun handle(
        actor: ICommandActor,
       @CommandArgument("<old-password>") oldPassword: String,
       @CommandArgument("<new-password>") newPassword: String
    ) = runBlocking{
        if (!authConfig.crackAllowed) {
            actor.sendMessage("§cCrack mode is currently disabled!")
            return@runBlocking
        }
        val proxyPlayer = VelocityCoreAPI.proxyServer.getPlayer(actor.uuid).get()
        if (proxyPlayer.isOnlineMode) {
            actor.sendMessage("§cYou are a premium player!")
            return@runBlocking
        }
        val player = VelocityCoreAPI.getPlayerManager().getPlayerByUUID(actor.uuid)
        if (player == null) {
            actor.sendMessage("§cYou are not registered!")
            return@runBlocking
        }
        if(player.authentication.crypt == null){
            player.authentication.crypt = UpdatableBCrypt(authConfig.passwordRounds)
        }
        if(!player.authentication.crypt!!.verifyHash(oldPassword, player.authentication.passwordHash)){
            actor.sendMessage("§cWrong password!")
            return@runBlocking
        }
        player.authentication.passwordHash = player.authentication.crypt!!.hash(newPassword)
        player.update()

        actor.sendMessage("§aPassword changed!")
    }

}
