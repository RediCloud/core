package net.dustrean.api.minestom

import kotlinx.coroutines.runBlocking
import net.dustrean.api.cloud.CloudCoreAPI
import net.dustrean.api.language.ILanguageBridge
import net.dustrean.api.minestom.command.MinestomCommandManager
import net.dustrean.api.minestom.language.MinestomLanguageBridge
import net.dustrean.api.minestom.utils.parser.PlayerParser
import net.dustrean.api.utils.fetcher.UniqueIdFetcher
import net.dustrean.api.utils.parser.string.StringParser
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerSpawnEvent

object MinestomCoreAPI : CloudCoreAPI() {

    private val languageBridge = MinestomLanguageBridge()

    private val playerNode = EventNode.type("player_node", EventFilter.PLAYER).apply {
        addListener(PlayerSpawnEvent::class.java) { event ->
            if (event.isFirstSpawn)
                runBlocking {
                    val player = getPlayerManager().getPlayerByUUID(event.player.uuid)
                    if (player == null) {
                        event.player.kick("§4An error occurred while loading your data. Please try again later or create a ticket.")
                        return@runBlocking
                    }
                    player.lastServer = getNetworkComponentInfo()
                    player.update()

                    UniqueIdFetcher.registerCache(event.player.uuid, player.name)
                }
        }
    }

    fun init() {
        MinecraftServer.getGlobalEventHandler().addChild(playerNode)
        StringParser.customTypeParsers.add(PlayerParser())
    }

    override fun getLanguageBridge(): MinestomLanguageBridge = languageBridge

    override fun getCommandManager() = MinestomCommandManager

}