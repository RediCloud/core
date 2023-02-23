package dev.redicloud.api.velocity.command.impl

import kotlinx.coroutines.runBlocking
import dev.redicloud.api.CoreAPI
import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.cloud.player.connectToFallback
import dev.redicloud.api.command.Command
import dev.redicloud.api.command.ICommandActor
import dev.redicloud.api.command.annotations.CommandArgument
import dev.redicloud.api.command.annotations.CommandSubPath
import dev.redicloud.api.player.PlayerSession
import dev.redicloud.api.utils.crypt.UpdatableBCrypt
import dev.redicloud.api.utils.fetcher.UniqueIdFetcher
import dev.redicloud.api.velocity.VelocityCoreAPI
import dev.redicloud.api.velocity.config.PlayerAuthConfig
import net.kyori.adventure.text.Component

class LoginCommand : Command("login", commandDescription = "Login to your account") {

    private var authConfig: PlayerAuthConfig

    init {
        runBlocking {
            authConfig = ICoreAPI.INSTANCE.configManager.getConfig("player-authentication", PlayerAuthConfig::class.java)
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
        val player = VelocityCoreAPI.playerManager.getPlayerByUUID(actor.uuid)
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
        VelocityCoreAPI.playerManager.onlineFetcher.add(actor.uuid)
        val session = PlayerSession().apply {
            ip = proxyPlayer.remoteAddress.address.hostAddress
            start = System.currentTimeMillis()
            authenticated = true
            premium = true
            proxyId = ICoreAPI.INSTANCE.networkComponentInfo
        }
        if (player.name != proxyPlayer.username) {
            VelocityCoreAPI.playerManager.nameFetcher.remove(player.name.lowercase())
            VelocityCoreAPI.playerManager.nameFetcher[proxyPlayer.username.lowercase()] = proxyPlayer.uniqueId
            player.nameHistory.add(System.currentTimeMillis() to proxyPlayer.username)
            player.name = proxyPlayer.username
            // TODO: PlayerNameUpdate Event CoreAPI
        }
        player.authentication.lastVerify = System.currentTimeMillis()
        player.lastProxy = ICoreAPI.getInstance<CoreAPI>().networkComponentInfo
        player.connected = true
        player.sessions.add(session)
        player.update()

        UniqueIdFetcher.registerCache(player.uuid, player.name)

        actor.sendMessage("§aYou are now logged in!")
        player.connectToFallback()
    }
}