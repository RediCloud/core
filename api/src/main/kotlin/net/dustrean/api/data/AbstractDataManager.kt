package net.dustrean.api.data

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.*
import net.dustrean.api.ICoreAPI
import net.dustrean.api.data.packet.DataActionType
import net.dustrean.api.data.packet.DataObjectPacket
import net.dustrean.api.data.packet.cache.DataCacheActionType
import net.dustrean.api.data.packet.cache.DataCachePacket
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.redis.IRedisConnection
import net.dustrean.api.redis.codec.JsonJacksonKotlinCodec
import org.slf4j.LoggerFactory
import reactor.core.publisher.whenComplete
import java.util.*

abstract class AbstractDataManager<T : AbstractDataObject>(
    private val prefix: String,
    private val connection: IRedisConnection,
    private val implClass: Class<T>
) : IDataManager<T> {

    companion object {
        val MANAGERS = mutableMapOf<String, AbstractDataManager<out AbstractDataObject>>()
        private val objectMapper = JsonMapper.builder()
            .addModule(KotlinModule.Builder().build())
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .defaultMergeable(true)
            .build()
        private val codec = JsonJacksonKotlinCodec(objectMapper)
    }

    val cacheScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        MANAGERS[prefix] = this
        ICoreAPI.INSTANCE.getPacketManager().registerPacket(DataCachePacket())
        ICoreAPI.INSTANCE.getPacketManager().registerPacket(DataObjectPacket())
    }

    val cachedObjects = mutableMapOf<UUID, T>()
    val parkedObjects = mutableMapOf<UUID, T>()
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun getCache(): List<T> {
        return cachedObjects.values.toList()
    }

    suspend fun sendPacket(identifier: UUID, action: DataCacheActionType, components: Set<NetworkComponentInfo>) {
        val packet = DataCachePacket()
        packet.identifier = identifier
        packet.managerPrefix = prefix
        packet.action = action
        packet.packetData.receiverComponent.addAll(components)
        packet.sendPacket()
    }

    suspend fun sendPacket(dataObject: T, action: DataActionType) {
        val packet = DataObjectPacket()
        packet.json = serialize(dataObject)
        packet.type = action
        packet.identifier = dataObject.getIdentifier()
        packet.managerPrefix = prefix
        val networkComponents =
            dataObject.getCacheHandler().getCacheNetworkComponents().toMutableSet()
        networkComponents.addAll(dataObject.getCacheHandler().getCurrentNetworkComponents())
        packet.packetData.receiverComponent.addAll(networkComponents)
        packet.sendPacket()
    }


    override fun getCache(identifier: UUID): T? {
        if (cachedObjects.containsKey(identifier)) {
            val cachedObject = cachedObjects[identifier]!!
            if (cachedObject.getValidator() != null) {
                if (cachedObject.getValidator()!!.isValid()) return cachedObject
                cachedObjects.remove(identifier)
                parkedObjects[identifier] = cachedObject
                cacheScope.launch {
                    val networkComponents =
                        cachedObject.getCacheHandler().getCacheNetworkComponents().toMutableSet()
                    networkComponents.addAll(cachedObject.getCacheHandler().getCurrentNetworkComponents())
                    sendPacket(identifier, DataCacheActionType.REMOVED, networkComponents)
                }
                return null
            }
            return cachedObject
        } else if (parkedObjects.containsKey(identifier)) {
            val parkedObject = parkedObjects[identifier]!!
            if (parkedObject.getValidator() != null) {
                if (parkedObject.getValidator()!!.isValid()) {
                    cachedObjects[identifier] = parkedObject
                    parkedObjects.remove(identifier)
                    cacheScope.launch {
                        val networkComponents = parkedObject.getCacheHandler().getCacheNetworkComponents()
                        sendPacket(identifier, DataCacheActionType.ADDED, networkComponents)
                    }
                    return parkedObject
                }
            }
        }
        return null
    }

    override fun unregisterCache() {
        cachedObjects.forEach { (_, cachedObject) ->
            if (cachedObject.getValidator() == null) return@forEach
            if (!cachedObject.getValidator()!!.isValid()) return@forEach
            cachedObjects.remove(cachedObject.getIdentifier())

            cacheScope.launch {
                val networkComponents =
                    cachedObject.getCacheHandler().getCacheNetworkComponents().toMutableSet()
                networkComponents.addAll(cachedObject.getCacheHandler().getCurrentNetworkComponents())
                sendPacket(cachedObject.getIdentifier(), DataCacheActionType.REMOVED, networkComponents)
            }

        }
    }

    override fun isCached(identifier: UUID): Boolean {
        return getCache(identifier) != null
    }

    fun containsCacheList(identifier: UUID): Boolean {
        return cachedObjects.containsKey(identifier)
    }

    fun containsParkedList(identifier: UUID): Boolean {
        return parkedObjects.containsKey(identifier)
    }

    override suspend fun getObject(identifier: UUID): T {
        val cache: T? = getCache(identifier)
        if (cache != null) {
            return cache
        }
        val key = "$prefix:$identifier"
        val bucket = connection.getRedissonClient().getBucket<T>(key)
        if(!bucket.isExists) throw IllegalArgumentException("Object with identifier $identifier does not exist!")
        val dataObject = bucket.get()
        if (dataObject.getValidator() == null || (dataObject.getValidator()?.isValid() == true)) {
            cachedObjects[dataObject.getIdentifier()] = dataObject
            sendPacket(
                identifier,
                DataCacheActionType.ADDED,
                dataObject.getCacheHandler().getCacheNetworkComponents()
            )
        } else {
            parkedObjects[dataObject.getIdentifier()] = dataObject
        }
        return dataObject
    }

    override suspend fun createObject(dataObject: T): T {
        val key = "$prefix:${dataObject.getIdentifier()}"
        val bucket = connection.getRedissonClient().getBucket<T>(key)
        if(existsObject(dataObject.getIdentifier())) throw IllegalArgumentException("Object with identifier ${dataObject.getIdentifier()} already exists!")
        bucket.set(dataObject)
        if (dataObject.getValidator() == null || (dataObject.getValidator()?.isValid() == true)) {
            cachedObjects[dataObject.getIdentifier()] = dataObject
        } else {
            parkedObjects[dataObject.getIdentifier()] = dataObject
        }
        sendPacket(dataObject, DataActionType.CREATE)
        return dataObject
    }

    override suspend fun updateObject(dataObject: T): T {
        val key = "$prefix:${dataObject.getIdentifier()}"
        val bucket = connection.getRedissonClient().getBucket<T>(key)
        if (!existsObject(dataObject.getIdentifier())) throw NoSuchElementException("Object with identifier ${dataObject.getIdentifier()} does not exist")
        bucket.set(dataObject)
        if (dataObject.getValidator() == null || (dataObject.getValidator()?.isValid() == true)) {
            cachedObjects[dataObject.getIdentifier()] = dataObject
            parkedObjects.remove(dataObject.getIdentifier())
        } else {
            parkedObjects[dataObject.getIdentifier()] = dataObject
        }
        sendPacket(dataObject, DataActionType.UPDATE)
        return dataObject
    }

    override suspend fun deleteObject(dataObject: T) {
        val key = "$prefix:${dataObject.getIdentifier()}"
        val bucket = connection.getRedissonClient().getBucket<T>(key)
        if (!existsObject(dataObject.getIdentifier())) throw NoSuchElementException("Object with identifier ${dataObject.getIdentifier()} does not exist")
        bucket.delete()
        cachedObjects.remove(dataObject.getIdentifier())
        parkedObjects.remove(dataObject.getIdentifier())
        sendPacket(dataObject, DataActionType.DELETE)
    }

    override suspend fun existsObject(identifier: UUID): Boolean {
        if (isCached(identifier)) {
            return true
        }
        val key = "$prefix:<$identifier"
        val bucket = connection.getRedissonClient().getBucket<T>(key)
        return bucket.isExists
    }

    fun serialize(dataObject: T): String {
        return objectMapper.writeValueAsString(dataObject)
    }

    fun deserialize(identifier: UUID?, json: String): T {
        if (identifier != null) {
            var dataObject: T? = null
            if (cachedObjects.containsKey(identifier)) dataObject = cachedObjects[identifier]
            if (parkedObjects.containsKey(identifier)) dataObject = parkedObjects[identifier]
            if (dataObject != null) {
                objectMapper.readerForUpdating(dataObject).readValue<T>(json)
                return dataObject
            }
        }
        val dataObject = objectMapper.readValue(json, implClass)
        if (dataObject.getValidator() == null || (dataObject.getValidator()?.isValid() == true)) {
            cachedObjects[dataObject.getIdentifier()] = dataObject
            cacheScope.launch {
                sendPacket(dataObject.getIdentifier(), DataCacheActionType.ADDED, dataObject.getCacheHandler().getCacheNetworkComponents())
            }
            return dataObject
        }
        parkedObjects[dataObject.getIdentifier()] = dataObject
        return dataObject
    }

    override fun getDataPrefix(): String {
        return prefix
    }

}