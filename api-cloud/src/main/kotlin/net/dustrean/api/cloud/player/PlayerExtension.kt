package net.dustrean.api.cloud.player

import eu.cloudnetservice.modules.bridge.player.executor.ServerSelectorType
import net.dustrean.api.cloud.utils.getCloudPlayerManager
import net.dustrean.api.cloud.utils.getCloudServiceProvider
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.player.IPlayer

fun IPlayer.connect(service: NetworkComponentInfo) {
    val cloudService = getCloudServiceProvider().service(service.identifier) ?: return
    getCloudPlayerManager().playerExecutor(uuid).connect(cloudService.name())
}

fun IPlayer.connectToGroup(group: String, serverSelectorType: ServerSelectorType) =
    getCloudPlayerManager().playerExecutor(uuid).connectToGroup(group, serverSelectorType)


fun IPlayer.connectToTask(task: String, serverSelectorType: ServerSelectorType) =
    getCloudPlayerManager().playerExecutor(uuid).connectToTask(task, serverSelectorType)

fun IPlayer.connectToFallback() = getCloudPlayerManager().playerExecutor(uuid).connectToFallback()