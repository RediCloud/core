package net.dustrean.api.cloud.player

import eu.cloudnetservice.modules.bridge.player.executor.ServerSelectorType
import net.dustrean.api.cloud.utils.getCloudPlayerManager
import net.dustrean.api.cloud.utils.getCloudServiceProvider
import net.dustrean.api.language.placeholder.Placeholder
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.player.IPlayer
import net.dustrean.api.player.PlayerManager
import java.util.*


suspend fun PlayerManager.getPlayerByUUID(uuid: UUID): IPlayer? {
    val player = this.getPlayerByUUID(uuid) ?: return null
    player.placeholders.add(Placeholder("server", value = {
        getCloudServiceProvider().service(player.lastServer.identifier)?.name() ?: "Unknown"
    }))
    player.placeholders.add(Placeholder("proxy", value = {
        getCloudServiceProvider().service(player.lastProxy.identifier)?.name() ?: "Unknown"
    }))
    return player
}

suspend fun PlayerManager.getPlayerByName(name: String): IPlayer? {
    val player = this.getPlayerByName(name) ?: return null
    player.placeholders.add(Placeholder("server", value = {
        getCloudServiceProvider().service(player.lastServer.identifier)?.name() ?: "Unknown"
    }))
    player.placeholders.add(Placeholder("proxy", value = {
        getCloudServiceProvider().service(player.lastProxy.identifier)?.name() ?: "Unknown"
    }))
    return player
}

fun IPlayer.connect(service: NetworkComponentInfo) {
    val cloudService = getCloudServiceProvider().service(service.identifier) ?: return
    getCloudPlayerManager().playerExecutor(uuid).connect(cloudService.name())
}

fun IPlayer.connectToGroup(group: String, serverSelectorType: ServerSelectorType) =
    getCloudPlayerManager().playerExecutor(uuid).connectToGroup(group, serverSelectorType)


fun IPlayer.connectToTask(task: String, serverSelectorType: ServerSelectorType) =
    getCloudPlayerManager().playerExecutor(uuid).connectToTask(task, serverSelectorType)

fun IPlayer.connectToFallback() = getCloudPlayerManager().playerExecutor(uuid).connectToFallback()