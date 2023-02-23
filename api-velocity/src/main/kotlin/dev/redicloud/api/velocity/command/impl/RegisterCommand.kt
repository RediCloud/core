package dev.redicloud.api.velocity.command.impl

import kotlinx.coroutines.runBlocking
import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.cloud.player.connectToFallback
import dev.redicloud.api.command.Command
import dev.redicloud.api.command.ICommandActor
import dev.redicloud.api.command.annotations.CommandArgument
import dev.redicloud.api.command.annotations.CommandSubPath
import dev.redicloud.api.player.Player
import dev.redicloud.api.player.PlayerAuthentication
import dev.redicloud.api.player.PlayerSession
import dev.redicloud.api.utils.crypt.UpdatableBCrypt
import dev.redicloud.api.utils.fetcher.UniqueIdFetcher
import dev.redicloud.api.velocity.VelocityCoreAPI
import dev.redicloud.api.velocity.config.PlayerAuthConfig

class RegisterCommand : Command("register", commandDescription = "Register your account") {

    private var authConfig: PlayerAuthConfig

    init {
        runBlocking {
            authConfig = ICoreAPI.INSTANCE.configManager.getConfig("player-authentication", PlayerAuthConfig::class.java)
        }
    }

    @CommandSubPath
    fun register(
        actor: ICommandActor,
        @CommandArgument("<password>") password: String,
        @CommandArgument("<passwordConfirm>") passwordConfirm: String
    ) = runBlocking {
        if (VelocityCoreAPI.playerManager.existsObject(actor.uuid)) {
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
                this.proxyId = VelocityCoreAPI.networkComponentInfo
            }
            this.authentication = authentication
            this.sessions.add(session)
            this.lastProxy = ICoreAPI.INSTANCE.networkComponentInfo
            nameHistory.add(System.currentTimeMillis() to proxyPlayer.username)
        }
        UniqueIdFetcher.registerCache(player.uuid, player.name)
        VelocityCoreAPI.playerManager.createObject(player)
        ICoreAPI.INSTANCE.playerManager.nameFetcher[proxyPlayer.username.lowercase()] = actor.uuid

        actor.sendMessage("§aYou are now registered!")
        player.connectToFallback()
    }

}