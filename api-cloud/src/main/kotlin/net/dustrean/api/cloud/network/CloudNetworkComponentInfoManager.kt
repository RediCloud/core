package net.dustrean.api.cloud.network

import net.dustrean.api.cloud.utils.getServiceProvider
import net.dustrean.api.cloud.utils.translateServiceEnvironment
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.network.NetworkComponentManager
import net.dustrean.api.network.NetworkComponentType
import net.dustrean.api.tasks.futures.FutureAction
import java.util.*

class CloudNetworkComponentInfoManager : NetworkComponentManager() {

    val components = mutableMapOf<String, NetworkComponentInfo>()

    override fun getComponentInfos(): List<NetworkComponentInfo> {
        getServiceProvider().services().let {
            return it.map { serviceInfo ->
                val type = translateServiceEnvironment(serviceInfo.serviceId().environment())
                val key = type.prefix + serviceInfo.serviceId().uniqueId().toString()
                if (components.containsKey(key)) return@map components[key]!!
                val component = NetworkComponentInfo(type, serviceInfo.serviceId().uniqueId())
                components[key] = component
                return@map component
            }
        }
    }

    override fun getComponentInfosAsync(): FutureAction<List<NetworkComponentInfo>> {
        val future = FutureAction<List<NetworkComponentInfo>>()
        getServiceProvider().servicesAsync().thenAccept {
            future.complete(it.map { serviceInfo ->
                val type = translateServiceEnvironment(serviceInfo.serviceId().environment())
                val key = type.prefix + serviceInfo.serviceId().uniqueId().toString()
                if (components.containsKey(key)) return@map components[key]!!
                val component = NetworkComponentInfo(type, serviceInfo.serviceId().uniqueId())
                components[key] = component
                return@map component
            })
        }
        return future
    }

    override fun getComponentInfo(uniqueId: UUID): NetworkComponentInfo? {
        if (components.containsKey(uniqueId.toString())) return components[uniqueId.toString()]
        getServiceProvider().service(uniqueId).let {
            if (it == null) throw NullPointerException("Service with UUID $uniqueId not found!")
            val type = translateServiceEnvironment(it.serviceId().environment())
            val key = type.prefix + it.serviceId().uniqueId().toString()
            if (components.containsKey(key)) return components[key]!!
            val component = NetworkComponentInfo(type, it.serviceId().uniqueId())
            components[key] = component
            return component
        }
    }

    override fun getComponentInfoAsync(key: String): FutureAction<NetworkComponentInfo> {
        val future = FutureAction<NetworkComponentInfo>()
        if (components.containsKey(key)) {
            future.complete(components[key]!!)
            return future
        }
        val splits = key.split("@")
        if (splits.size != 2) throw IllegalArgumentException("Invalid key format!")
        try {
            val type = NetworkComponentType.valueOf(splits[0])
            val uniqueId = UUID.fromString(splits[1])
            getServiceProvider().serviceAsync(uniqueId).thenAccept {
                if (it == null) throw NullPointerException("Service with UUID $key not found!")
                if (components.containsKey(key)) future.complete(components[key]!!)
                val component = NetworkComponentInfo(type, it.serviceId().uniqueId())
                components[key] = component
                future.complete(component)
            }
        } catch (e: Exception) {
            future.completeExceptionally(e)
        }
        return future
    }

    override fun getComponentInfo(key: String): NetworkComponentInfo? {
        if (components.containsKey(key)) return components[key]
        val splits = key.split("@")
        if (splits.size != 2) throw IllegalArgumentException("Invalid key format!")
        val type = NetworkComponentType.valueOf(splits[0])
        val uniqueId = UUID.fromString(splits[1])
        getServiceProvider().service(uniqueId).let {
            if (it == null) throw NullPointerException("Service with UUID $key not found!")
            val component = NetworkComponentInfo(type, it.serviceId().uniqueId())
            components[key] = component
            return component
        }
    }

    override fun getComponentInfoAsync(uniqueId: UUID): FutureAction<NetworkComponentInfo> {
        val future = FutureAction<NetworkComponentInfo>()
        if (components.containsKey(uniqueId.toString())) {
            future.complete(components[uniqueId.toString()]!!)
            return future
        }
        getServiceProvider().serviceAsync(uniqueId).thenAccept {
            if (it == null) throw NullPointerException("Service with UUID $uniqueId not found!")
            val type = translateServiceEnvironment(it.serviceId().environment())
            val key = type.prefix + it.serviceId().uniqueId().toString()
            if (components.containsKey(key)) future.complete(components[key]!!)
            val component = NetworkComponentInfo(type, it.serviceId().uniqueId())
            components[key] = component
            future.complete(component)
        }
        return future
    }

    override fun getComponentInfos(type: NetworkComponentType): List<NetworkComponentInfo> {
        getServiceProvider().services().let {
            return it.filter { serviceInfo ->
                translateServiceEnvironment(serviceInfo.serviceId().environment()) == type
            }.map { serviceInfo ->
                val key = type.prefix + serviceInfo.serviceId().uniqueId().toString()
                if (components.containsKey(key)) return@map components[key]!!
                val component = NetworkComponentInfo(type, serviceInfo.serviceId().uniqueId())
                components[key] = component
                return@map component
            }
        }
    }

    override fun getComponentInfosAsync(type: NetworkComponentType): FutureAction<List<NetworkComponentInfo>> {
        val future = FutureAction<List<NetworkComponentInfo>>()
        getServiceProvider().servicesAsync().thenAccept {
            future.complete(it.filter { serviceInfo ->
                translateServiceEnvironment(serviceInfo.serviceId().environment()) == type
            }.map { serviceInfo ->
                val key = type.prefix + serviceInfo.serviceId().uniqueId().toString()
                if (components.containsKey(key)) return@map components[key]!!
                val component = NetworkComponentInfo(type, serviceInfo.serviceId().uniqueId())
                components[key] = component
                return@map component
            })
        }
        return future
    }

}