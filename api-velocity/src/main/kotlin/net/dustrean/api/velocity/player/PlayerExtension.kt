package net.dustrean.api.velocity.player

import net.dustrean.api.cloud.utils.getCloudServiceProvider
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.player.IPlayer
import net.dustrean.api.player.Player
import net.dustrean.api.velocity.VelocityCoreAPI
import kotlin.jvm.optionals.getOrNull

@OptIn(ExperimentalStdlibApi::class)
suspend fun IPlayer.connect(service: NetworkComponentInfo) {
    val player = VelocityCoreAPI.proxyServer.getPlayer(uuid).getOrNull() ?: return
    val service = getCloudServiceProvider().service(service.identifier) ?: return
    val server = VelocityCoreAPI.proxyServer.getServer(service.name()).getOrNull() ?: return
    player.createConnectionRequest(server).connect().get()
}