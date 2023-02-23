package dev.redicloud.api.data

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import kotlinx.coroutines.*
import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.data.packet.DataActionType
import dev.redicloud.api.data.packet.DataObjectPacket
import dev.redicloud.api.data.packet.cache.DataCacheActionType
import dev.redicloud.api.data.packet.cache.DataCachePacket
import dev.redicloud.api.event.CoreEvent
import dev.redicloud.api.event.EventType
import dev.redicloud.api.network.NetworkComponentInfo
import dev.redicloud.api.redis.IRedisConnection
import dev.redicloud.api.redis.codec.GsonIgnore
import org.slf4j.LoggerFactory
import java.util.*

abstract class AbstractDataManager<T : AbstractDataObject>(
    private val prefix: String,
    private val connection: IRedisConnection,
    private val implClass: Class<T>
) : IDataManager<T> {
    data class ObjectUpdateEvent <T>(
        val prefix: String,
        val classType: Class<T>,
        val obj: T
    ): CoreEvent(EventType.GLOBAL)

    fun getUpdateEvent(obj: Any): ObjectUpdateEvent<T> {
        return ObjectUpdateEvent(prefix, implClass, obj as T)
    }

    companion object {
        val MANAGERS = mutableMapOf<String, AbstractDataManager<out AbstractDataObject>>()
        /*private val objectMapper = JsonMapper.builder()
            .addModule(KotlinModule.Builder().build())
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .defaultMergeable(true)
            .build()*/
        private val objectMapper: Gson = GsonBuilder().addSerializationExclusionStrategy(object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes?): Boolean =
                f?.getAnnotation(Expose::class.java)?.serialize == false || f?.getAnnotation(GsonIgnore::class.java) != null

            override fun shouldSkipClass(clazz: Class<*>?): Boolean = false

        }).addSerializationExclusionStrategy(object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes?): Boolean =
                f?.getAnnotation(Expose::class.java)?.deserialize == false || f?.getAnnotation(GsonIgnore::class.java) != null

            override fun shouldSkipClass(clazz: Class<*>?): Boolean = false
        }).disableHtmlEscaping().create()
    }

    private val cacheScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        MANAGERS[prefix] = this
        ICoreAPI.INSTANCE.packetManager.registerPacket(DataCachePacket())
        ICoreAPI.INSTANCE.packetManager.registerPacket(DataObjectPacket())
    }
    val cachedObjects = mutableMapOf<UUID, T>()
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
        packet.identifier = dataObject.identifier
        packet.managerPrefix = prefix
        val networkComponents =
            dataObject.cacheHandler.getCacheNetworkComponents().toMutableSet()
        networkComponents.addAll(dataObject.cacheHandler.getCurrentNetworkComponents())
        packet.packetData.receiverComponent.addAll(networkComponents)
        packet.sendPacket()
    }

    override fun getCache(identifier: UUID): T? {
        if (cachedObjects.containsKey(identifier)) {
            val cachedObject = cachedObjects[identifier]!!
            if (cachedObject.validator != null) {
                if (cachedObject.validator!!.isValid()) return cachedObject
                cachedObjects.remove(identifier)
                cacheScope.launch {
                    val networkComponents =
                        cachedObject.cacheHandler.getCacheNetworkComponents().toMutableSet()
                    networkComponents.addAll(cachedObject.cacheHandler.getCurrentNetworkComponents())
                    sendPacket(identifier, DataCacheActionType.REMOVED, networkComponents)
                }
                return null
            }
            return cachedObject
        }
        return null
    }

    override fun unregisterCache() {
        cachedObjects.forEach { (_, cachedObject) ->
            if (cachedObject.validator == null) return@forEach
            if (!cachedObject.validator!!.isValid()) return@forEach
            cachedObjects.remove(cachedObject.identifier)

            cacheScope.launch {
                val networkComponents =
                    cachedObject.cacheHandler.getCacheNetworkComponents().toMutableSet()
                networkComponents.addAll(cachedObject.cacheHandler.getCurrentNetworkComponents())
                sendPacket(cachedObject.identifier, DataCacheActionType.REMOVED, networkComponents)
            }

        }
    }

    override fun isCached(identifier: UUID): Boolean {
        return getCache(identifier) != null
    }

    fun containsCacheList(identifier: UUID): Boolean {
        return cachedObjects.containsKey(identifier)
    }

    override suspend fun getObject(identifier: UUID): T {
        val cache: T? = getCache(identifier)
        if (cache != null) {
            return cache
        }
        val key = "$prefix:$identifier"
        val bucket = connection.getRedissonClient().getBucket<T>(key)
        if (!bucket.isExists) throw NoSuchElementException("Object with identifier $identifier does not exist!")
        val dataObject = bucket.get()
        if (dataObject.validator == null || (dataObject.validator?.isValid() == true)) {
            cachedObjects[dataObject.identifier] = dataObject
            sendPacket(
                identifier,
                DataCacheActionType.ADDED,
                dataObject.cacheHandler.getCacheNetworkComponents()
            )
        }
        return dataObject
    }

    override suspend fun createObject(dataObject: T): T {
        val key = "$prefix:${dataObject.identifier}"
        val bucket = connection.getRedissonClient().getBucket<T>(key)
        if (existsObject(dataObject.identifier)) throw IllegalArgumentException("Object with identifier ${dataObject.identifier} already exists!")
        bucket.set(dataObject)
        if (dataObject.validator == null || (dataObject.validator?.isValid() == true)) {
            cachedObjects[dataObject.identifier] = dataObject
        }
        sendPacket(dataObject, DataActionType.CREATE)
        return dataObject
    }

    override suspend fun updateObject(dataObject: T): T {
        val key = "$prefix:${dataObject.identifier}"
        val bucket = connection.getRedissonClient().getBucket<T>(key)
        if (!existsObject(dataObject.identifier)) throw NoSuchElementException("Object with identifier ${dataObject.identifier} does not exist")
        bucket.set(dataObject)
        if (dataObject.validator == null || (dataObject.validator?.isValid() == true)) {
            cachedObjects[dataObject.identifier] = dataObject
        }
        sendPacket(dataObject, DataActionType.UPDATE)
        return dataObject
    }

    override suspend fun deleteObject(dataObject: T) {
        val key = "$prefix:${dataObject.identifier}"
        val bucket = connection.getRedissonClient().getBucket<T>(key)
        if (!existsObject(dataObject.identifier)) throw NoSuchElementException("Object with identifier ${dataObject.identifier} does not exist")
        bucket.delete()
        cachedObjects.remove(dataObject.identifier)
        sendPacket(dataObject, DataActionType.DELETE)
    }

    override suspend fun existsObject(identifier: UUID): Boolean {
        if (isCached(identifier)) {
            return true
        }
        val key = "$prefix:$identifier"
        val bucket = connection.getRedissonClient().getBucket<T>(key)
        return bucket.isExists
    }

    fun serialize(dataObject: T): String {
        return objectMapper.toJson(dataObject)
    }

    fun deserialize(json: String): T {
        val dataObject = objectMapper.fromJson(json, implClass)
        if (dataObject.validator == null || (dataObject.validator?.isValid() == true)) {
            cachedObjects[dataObject.identifier] = dataObject
            cacheScope.launch {
                sendPacket(
                    dataObject.identifier,
                    DataCacheActionType.ADDED,
                    dataObject.cacheHandler.getCacheNetworkComponents()
                )
            }
            return dataObject
        }
        return dataObject
    }

    override fun getDataPrefix(): String {
        return prefix
    }

}