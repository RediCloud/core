package dev.redicloud.api.velocity.event

import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.event.connection.PostLoginEvent
import com.velocitypowered.api.event.connection.PreLoginEvent
import com.velocitypowered.api.event.player.ServerPreConnectEvent
import kotlinx.coroutines.runBlocking
import dev.redicloud.api.CoreAPI
import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.cloud.utils.cloudServiceProvider
import dev.redicloud.api.cloud.utils.cloudTaskProvider
import dev.redicloud.api.player.Player
import dev.redicloud.api.player.PlayerAuthentication
import dev.redicloud.api.player.PlayerManager
import dev.redicloud.api.player.PlayerSession
import dev.redicloud.api.utils.extension.isPremium
import dev.redicloud.api.utils.fetcher.UniqueIdFetcher
import dev.redicloud.api.velocity.command.impl.ChangePasswordCommand
import dev.redicloud.api.velocity.command.impl.LoginCommand
import dev.redicloud.api.velocity.command.impl.RegisterCommand
import dev.redicloud.api.velocity.config.PlayerAuthConfig
import net.kyori.adventure.text.Component
import java.io.IOException
import java.util.regex.Pattern
import kotlin.time.Duration.Companion.seconds

class PlayerEvents(private val playerManager: PlayerManager) {

    private var authConfig: PlayerAuthConfig
    private val namePattern = Pattern.compile("^[a-zA-Z0-9_]{2,16}$")

    init {
        runBlocking {
            authConfig = ICoreAPI.INSTANCE.configManager
                .getConfigOrPut("player-authentication", PlayerAuthConfig::class.java) {
                    PlayerAuthConfig("player-authentication")
                }

            if (authConfig.crackAllowed) {
                ICoreAPI.INSTANCE.commandManager.registerCommand(RegisterCommand())
                ICoreAPI.INSTANCE.commandManager.registerCommand(LoginCommand())
                ICoreAPI.INSTANCE.commandManager.registerCommand(ChangePasswordCommand())
            }
        }
    }

    @Subscribe(order = PostOrder.FIRST)
    fun onPreLogin(event: PreLoginEvent) = runBlocking {
        val name = event.username

        if (!authConfig.crackAllowed) {
            event.result = PreLoginEvent.PreLoginComponentResult.forceOnlineMode()
            return@runBlocking
        }

        if (name.length > 16) {
            event.result = PreLoginEvent.PreLoginComponentResult.denied(Component.text("Username too long"))
            return@runBlocking
        }

        if (name.length < 3) {
            event.result = PreLoginEvent.PreLoginComponentResult.denied(Component.text("Username too short"))
            return@runBlocking
        }

        if (!namePattern.matcher(name).matches()) {
            event.result = PreLoginEvent.PreLoginComponentResult.denied(Component.text("Invalid username"))
            return@runBlocking
        }

        try {
            val premiumUniqueId = UniqueIdFetcher.fetchUniqueId(name, false)
            if (premiumUniqueId == null) {
                event.result = PreLoginEvent.PreLoginComponentResult.forceOfflineMode()
            } else {
                event.result = PreLoginEvent.PreLoginComponentResult.forceOnlineMode()
            }
        } catch (e: IOException) {
            event.result =
                PreLoginEvent.PreLoginComponentResult.denied(Component.text("Error while connecting to external service!\nPlease try again later."))
        }
    }

    @Subscribe(order = PostOrder.FIRST)
    fun onPlayerJoin(event: LoginEvent) = runBlocking j@{

        val uniqueId = event.player.uniqueId
        val name = event.player.username

        if (!event.player.isOnlineMode) {
            if (uniqueId.isPremium(name)) {
                event.player.disconnect(Component.text("You are a premium player, but you are not authenticated!\nPlease relogin!"))
                return@j
            }
            return@j
        }

        val player = playerManager.getPlayerByUUID(
            event.player.uniqueId
        )
        playerManager.onlineFetcher.add(event.player.uniqueId)

        val authentication = PlayerAuthentication().apply {
            cracked = false
            ip = event.player.remoteAddress.address.hostAddress
            lastVerify = System.currentTimeMillis()
            loginProcess = false
        }
        val session = PlayerSession().apply {
            ip = event.player.remoteAddress.address.hostAddress
            start = System.currentTimeMillis()
            authenticated = true
            premium = true
            proxyId = ICoreAPI.INSTANCE.networkComponentInfo
        }

        if (player != null) {
            if (player.name != event.player.username) {
                playerManager.nameFetcher.remove(player.name.lowercase())
                playerManager.nameFetcher[event.player.username.lowercase()] = event.player.uniqueId
                player.nameHistory.add(System.currentTimeMillis() to event.player.username)
                player.name = event.player.username
                // TODO: PlayerNameUpdate Event CoreAPI
            }
            player.authentication = authentication
            player.lastProxy = ICoreAPI.getInstance<CoreAPI>().networkComponentInfo
            player.connected = true
            player.sessions.add(session)
            player.update()
            return@j
        }
        playerManager.nameFetcher[event.player.username.lowercase()] = event.player.uniqueId
        ICoreAPI.getInstance<CoreAPI>().playerManager.createObject(Player(
            event.player.uniqueId, event.player.username, connected = true
        ).apply {
            nameHistory.add(System.currentTimeMillis() to event.player.username)
            this.authentication = authentication
            lastProxy = ICoreAPI.INSTANCE.networkComponentInfo
            sessions.add(session)
        })
    }

    @Subscribe(order = PostOrder.FIRST)
    fun onPostLogin(event: PostLoginEvent) = runBlocking {
        if (event.player.isOnlineMode) return@runBlocking
        val player = playerManager.getPlayerByUUID(event.player.uniqueId) ?: return@runBlocking
        if (player.authentication.isLoggedIn(player)) {
            event.player.sendMessage(Component.text("You are logged in as ${player.name}!"))
        }
    }

    @Subscribe(order = PostOrder.FIRST)
    fun onPreServerConnect(event: ServerPreConnectEvent) {
        val target = event.originalServer
        val player = runBlocking {
            ICoreAPI.INSTANCE.playerManager.getPlayerByUUID(event.player.uniqueId)
        }
        if (player?.authentication?.isLoggedIn(player) == true) return
        if (!target.serverInfo.name.startsWith(authConfig.verifyTask)) {
            val task = cloudTaskProvider.serviceTask(authConfig.verifyTask)
            if (task == null || cloudServiceProvider.servicesByTask(authConfig.verifyTask).isEmpty()) {
                event.player.sendMessage(Component.text("§cThe authentication server is currently not available!"))
                event.result = ServerPreConnectEvent.ServerResult.denied()
                return
            }
        }
    }

    @Subscribe(order = PostOrder.LAST)
    fun onPlayerDisconnect(event: DisconnectEvent) {
        runBlocking {
            val player = runBlocking {
                playerManager.getPlayerByUUID(
                    event.player.uniqueId
                )
            }
            if (player != null) {
                val session = player.getCurrentSession()
                if (session != null) {
                    session.end = System.currentTimeMillis()
                    if (session.getDuration() < 5.seconds.inWholeMilliseconds) player.sessions.removeIf { it == session }
                }
                player.update()
            }
            playerManager.onlineFetcher.remove(event.player.uniqueId)
        }
    }

}