package net.dustrean.api.velocity.command.impl

import kotlinx.coroutines.runBlocking
import net.dustrean.api.ICoreAPI
import net.dustrean.api.cloud.player.connectToFallback
import net.dustrean.api.command.Command
import net.dustrean.api.command.ICommandActor
import net.dustrean.api.command.annotations.CommandArgument
import net.dustrean.api.command.annotations.CommandSubPath
import net.dustrean.api.player.Player
import net.dustrean.api.player.PlayerAuthentication
import net.dustrean.api.player.PlayerSession
import net.dustrean.api.utils.crypt.UpdatableBCrypt
import net.dustrean.api.velocity.VelocityCoreAPI
import net.dustrean.api.velocity.config.PlayerAuthConfig

class RegisterCommand : Command("register", commandDescription = "Register your account") {

    private var authConfig: PlayerAuthConfig

    init {
        runBlocking {
            authConfig = ICoreAPI.INSTANCE.getConfigManager().getConfig("player-authentication")
        }
    }

    @CommandSubPath
    fun register(
        actor: ICommandActor,
        @CommandArgument("<password>") password: String,
        @CommandArgument("<password>") passwordConfirm: String
    ) = runBlocking {
        if (VelocityCoreAPI.getPlayerManager().isCached(actor.uuid)) {
            actor.sendMessage("§cYou are already registered!")
            return@runBlocking
        }
        if (!authConfig.crackAllowed) {
            actor.sendMessage("§cCrack mode is currently disabled!")
            return@runBlocking
        }
        if (password != passwordConfirm) {
            actor.sendMessage("§cPasswords do not match!")
            return@runBlocking
        }
        if (password.length < authConfig.minPasswordLength) {
            actor.sendMessage("§cPassword is too short!")
            return@runBlocking
        }
        if (password.length > authConfig.maxPasswordLength) {
            actor.sendMessage("§cPassword is too long!")
            return@runBlocking
        }
        val proxyPlayer = VelocityCoreAPI.proxyServer.getPlayer(actor.uuid).get()
        if (proxyPlayer.isOnlineMode) {
            actor.sendMessage("§cYou are a premium player!")
            return@runBlocking
        }
        if (!authConfig.passwordCanContainsPlayerName && password.contains(proxyPlayer.username, true)) {
            actor.sendMessage("§cPassword cannot contains your name!")
            return@runBlocking
        }

        val player = Player(
            uuid = actor.uuid, name = proxyPlayer.username, connected = true
        ).apply {
            val authentication = PlayerAuthentication().apply {
                this.crypt = UpdatableBCrypt(authConfig.passwordRounds)
                this.passwordHash = crypt!!.hash(password)
                this.passwordLogRounds = authConfig.passwordRounds
                this.loginProcess = false
                this.cracked = true
                this.ip = proxyPlayer.remoteAddress.address.hostAddress
                this.lastVerify = System.currentTimeMillis()
            }
            val session = PlayerSession().apply {
                this.ip = proxyPlayer.remoteAddress.address.hostAddress
                this.start = System.currentTimeMillis()
                this.authenticated = true
                this.premium = false
                this.proxyId = VelocityCoreAPI.getNetworkComponentInfo()
            }
            this.authentication = authentication
            this.sessions.add(System.currentTimeMillis() to session)
            this.lastProxy = ICoreAPI.INSTANCE.getNetworkComponentInfo()
            nameHistory.add(System.currentTimeMillis() to proxyPlayer.username)
        }
        VelocityCoreAPI.getPlayerManager().createObject(player)
        ICoreAPI.INSTANCE.getPlayerManager().nameFetcher[proxyPlayer.username.lowercase()] = actor.uuid

        actor.sendMessage("§aYou are now registered!")
        player.connectToFallback()
    }

}