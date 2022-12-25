package net.dustrean.api.cloud.utils

import eu.cloudnetservice.driver.CloudNetDriver
import eu.cloudnetservice.driver.provider.CloudServiceProvider
import eu.cloudnetservice.driver.provider.GroupConfigurationProvider
import eu.cloudnetservice.driver.provider.ServiceTaskProvider
import eu.cloudnetservice.driver.service.ServiceEnvironmentType
import eu.cloudnetservice.modules.bridge.player.PlayerManager
import eu.cloudnetservice.wrapper.Wrapper
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.network.NetworkComponentType
import net.dustrean.api.player.IPlayerManager

fun getCurrentNetworkComponent(): NetworkComponentInfo {
    val serviceInfo = Wrapper.instance().lastServiceInfo()
    val environment: NetworkComponentType = when (serviceInfo.serviceId().environment()) {
        ServiceEnvironmentType.MINESTOM -> NetworkComponentType.MINESTOM
        ServiceEnvironmentType.VELOCITY -> NetworkComponentType.VELOCITY
        ServiceEnvironmentType.MINECRAFT_SERVER -> NetworkComponentType.PAPER
        else -> throw Exception("Unknown environment")
    }

    return NetworkComponentInfo(environment, serviceInfo.serviceId().uniqueId())
}

fun getCloudServiceProvider(): CloudServiceProvider =
    CloudNetDriver.instance<CloudNetDriver>().cloudServiceProvider()

fun getCloudTaskProvider(): ServiceTaskProvider =
    CloudNetDriver.instance<CloudNetDriver>().serviceTaskProvider()

fun getCloudGroupProvider(): GroupConfigurationProvider =
    CloudNetDriver.instance<CloudNetDriver>().groupConfigurationProvider()

fun getCloudPlayerManager(): PlayerManager =
    CloudNetDriver.instance<CloudNetDriver>().serviceRegistry().firstProvider(PlayerManager::class.java)

fun translateServiceEnvironment(environment: ServiceEnvironmentType): NetworkComponentType {
    var componentType: NetworkComponentType? = null
    when (environment) {
        ServiceEnvironmentType.MINESTOM -> componentType = NetworkComponentType.MINESTOM
        ServiceEnvironmentType.VELOCITY -> componentType = NetworkComponentType.VELOCITY
        ServiceEnvironmentType.MINECRAFT_SERVER -> componentType = NetworkComponentType.PAPER
    }
    if (componentType == null) throw Exception("Unknown environment")
    return componentType
}