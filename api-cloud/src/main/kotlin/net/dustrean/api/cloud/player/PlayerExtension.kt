package net.dustrean.api.cloud.player

import eu.cloudnetservice.modules.bridge.player.executor.ServerSelectorType
import net.dustrean.api.cloud.utils.cloudPlayerManager
import net.dustrean.api.cloud.utils.cloudServiceProvider
import net.dustrean.api.language.placeholder.Placeholder
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.player.IPlayer
import net.dustrean.api.player.PlayerManager
import java.util.*


suspend fun PlayerManager.getPlayerByUUID(uuid: UUID): IPlayer? {
    val player = this.getPlayerByUUID(uuid) ?: return null
    player.placeholders.add(Placeholder("server", value = {
        cloudServiceProvider.service(player.lastServer.identifier)?.name() ?: "Unknown"
    }))
    player.placeholders.add(Placeholder("proxy", value = {
        cloudServiceProvider.service(player.lastProxy.identifier)?.name() ?: "Unknown"
    }))
    return player
}

suspend fun PlayerManager.getPlayerByName(name: String): IPlayer? {
    val player = this.getPlayerByName(name) ?: return null
    player.placeholders.add(Placeholder("server", value = {
        cloudServiceProvider.service(player.lastServer.identifier)?.name() ?: "Unknown"
    }))
    player.placeholders.add(Placeholder("proxy", value = {
        cloudServiceProvider.service(player.lastProxy.identifier)?.name() ?: "Unknown"
    }))
    return player
}

fun IPlayer.connect(service: NetworkComponentInfo) {
    val cloudService = cloudServiceProvider.service(service.identifier) ?: return
    cloudPlayerManager.playerExecutor(uuid).connect(cloudService.name())
}

fun IPlayer.connectToGroup(group: String, serverSelectorType: ServerSelectorType) =
    cloudPlayerManager.playerExecutor(uuid).connectToGroup(group, serverSelectorType)


fun IPlayer.connectToTask(task: String, serverSelectorType: ServerSelectorType) =
    cloudPlayerManager.playerExecutor(uuid).connectToTask(task, serverSelectorType)

fun IPlayer.connectToFallback() = cloudPlayerManager.playerExecutor(uuid).connectToFallback()