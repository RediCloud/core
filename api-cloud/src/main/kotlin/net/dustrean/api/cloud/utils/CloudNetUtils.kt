package net.dustrean.api.cloud.utils

import eu.cloudnetservice.driver.CloudNetDriver
import eu.cloudnetservice.driver.provider.CloudServiceProvider
import eu.cloudnetservice.driver.service.ServiceEnvironmentType
import eu.cloudnetservice.wrapper.Wrapper
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.network.NetworkComponentType

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

fun getServiceProvider(): CloudServiceProvider {
    return CloudNetDriver.instance<CloudNetDriver>().cloudServiceProvider()
}

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