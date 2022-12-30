package net.dustrean.api.velocity.command.impl

import kotlinx.coroutines.runBlocking
import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.cloud.player.connectToFallback
import net.dustrean.api.command.Command
import net.dustrean.api.command.ICommandActor
import net.dustrean.api.command.annotations.CommandArgument
import net.dustrean.api.command.annotations.CommandSubPath
import net.dustrean.api.player.PlayerSession
import net.dustrean.api.utils.crypt.UpdatableBCrypt
import net.dustrean.api.velocity.VelocityCoreAPI
import net.dustrean.api.velocity.config.PlayerAuthConfig
import net.kyori.adventure.text.Component

class LoginCommand : Command("login", commandDescription = "Login to your account") {

    private var authConfig: PlayerAuthConfig

    init {
        runBlocking {
            authConfig = ICoreAPI.INSTANCE.getConfigManager().getConfig("player-authentication", PlayerAuthConfig::class.java)
        }
    }

    @CommandSubPath
    fun handle(
        actor: ICommandActor,
        @CommandArgument("<password>") password: String
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
        val player = VelocityCoreAPI.getPlayerManager().getPlayerByUUID(actor.uuid)
        if (player == null) {
            actor.sendMessage("§cYou are not registered!")
            return@runBlocking
        }
        if (player.authentication.crypt == null) {
            player.authentication.crypt = UpdatableBCrypt(authConfig.passwordRounds)
        }
        //TODO add delay, kick after 3 fails
        if (!player.authentication.crypt!!.verifyHash(password, player.authentication.passwordHash)) {
            proxyPlayer.disconnect(Component.text("§cWrong password!"))
            return@runBlocking
        }
        VelocityCoreAPI.getPlayerManager().onlineFetcher.add(actor.uuid)
        val session = PlayerSession().apply {
            ip = proxyPlayer.remoteAddress.address.hostAddress
            start = System.currentTimeMillis()
            authenticated = true
            premium = true
            proxyId = ICoreAPI.INSTANCE.getNetworkComponentInfo()
        }
        if (player.name != proxyPlayer.username) {
            VelocityCoreAPI.getPlayerManager().nameFetcher.remove(player.name.lowercase())
            VelocityCoreAPI.getPlayerManager().nameFetcher[proxyPlayer.username.lowercase()] = proxyPlayer.uniqueId
            player.nameHistory.add(System.currentTimeMillis() to proxyPlayer.username)
            player.name = proxyPlayer.username
            // TODO: PlayerNameUpdate Event CoreAPI
        }
        player.authentication.lastVerify = System.currentTimeMillis()
        player.lastProxy = ICoreAPI.getInstance<CoreAPI>().getNetworkComponentInfo()
        player.connected = true
        player.sessions.add(session)
        player.update()

        actor.sendMessage("§aYou are now logged in!")
        player.connectToFallback()
    }
}