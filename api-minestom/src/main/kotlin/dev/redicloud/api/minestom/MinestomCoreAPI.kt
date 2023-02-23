package dev.redicloud.api.minestom

import kotlinx.coroutines.runBlocking
import dev.redicloud.api.cloud.CloudCoreAPI
import dev.redicloud.api.command.ICommandManager
import dev.redicloud.api.language.ILanguageBridge
import dev.redicloud.api.minestom.command.MinestomCommandManager
import dev.redicloud.api.minestom.language.MinestomLanguageBridge
import dev.redicloud.api.minestom.utils.parser.PlayerParser
import dev.redicloud.api.utils.fetcher.UniqueIdFetcher
import dev.redicloud.api.utils.parser.string.StringParser
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerSpawnEvent

object MinestomCoreAPI : dev.redicloud.api.cloud.CloudCoreAPI() {
    override val commandManager: ICommandManager = MinestomCommandManager
    override val languageBridge = MinestomLanguageBridge()

    private val playerNode = EventNode.type("player_node", EventFilter.PLAYER).apply {
        addListener(PlayerSpawnEvent::class.java) { event ->
            if (event.isFirstSpawn)
                runBlocking {
                    val player = playerManager.getPlayerByUUID(event.player.uuid)
                    if (player == null) {
                        event.player.kick("§4An error occurred while loading your data. Please try again later or create a ticket.")
                        return@runBlocking
                    }
                    player.lastServer = networkComponentInfo
                    player.update()

                    UniqueIdFetcher.registerCache(event.player.uuid, player.name)
                }
        }
    }

    fun init() {
        MinecraftServer.getGlobalEventHandler().addChild(playerNode)
        StringParser.customTypeParsers.add(PlayerParser())
    }

}