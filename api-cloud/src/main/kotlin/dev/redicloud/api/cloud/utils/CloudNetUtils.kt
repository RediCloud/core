package dev.redicloud.api.cloud.utils

import eu.cloudnetservice.driver.inject.InjectionLayer
import eu.cloudnetservice.driver.provider.CloudServiceProvider
import eu.cloudnetservice.driver.provider.GroupConfigurationProvider
import eu.cloudnetservice.driver.provider.ServiceTaskProvider
import eu.cloudnetservice.driver.registry.ServiceRegistry
import eu.cloudnetservice.driver.service.ServiceEnvironmentType
import eu.cloudnetservice.driver.service.ServiceInfoSnapshot
import eu.cloudnetservice.modules.bridge.player.PlayerManager
import eu.cloudnetservice.wrapper.holder.ServiceInfoHolder
import dev.redicloud.api.network.NetworkComponentInfo
import dev.redicloud.api.network.NetworkComponentType


val cloudServiceProvider: CloudServiceProvider = InjectionLayer.ext().instance(CloudServiceProvider::class.java)
val cloudTaskProvider: ServiceTaskProvider = InjectionLayer.ext().instance(ServiceTaskProvider::class.java)
val cloudGroupConfigurationProvider: GroupConfigurationProvider = InjectionLayer.ext().instance(GroupConfigurationProvider::class.java)
val cloudPlayerManager: PlayerManager = ServiceRegistry.first(PlayerManager::class.java)
val cloudServiceInfoHolder: ServiceInfoHolder = InjectionLayer.ext().instance(ServiceInfoHolder::class.java)

fun getCurrentNetworkComponent(): NetworkComponentInfo {
    val serviceInfo = cloudServiceInfoHolder.lastServiceInfo()
    val environment: NetworkComponentType = when (serviceInfo.serviceId().environment()) {
        ServiceEnvironmentType.MINESTOM -> NetworkComponentType.MINESTOM
        ServiceEnvironmentType.VELOCITY -> NetworkComponentType.VELOCITY
        ServiceEnvironmentType.MINECRAFT_SERVER -> NetworkComponentType.PAPER
        else -> throw Exception("Unknown environment")
    }

    return NetworkComponentInfo(environment, serviceInfo.serviceId().uniqueId())
}

val currentCloudServiceInfo: ServiceInfoSnapshot
    get() = cloudServiceInfoHolder.serviceInfo()

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