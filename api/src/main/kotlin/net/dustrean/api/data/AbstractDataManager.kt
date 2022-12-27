package net.dustrean.api.data

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import kotlinx.coroutines.*
import net.bytebuddy.build.ToStringPlugin.Exclude
import net.dustrean.api.ICoreAPI
import net.dustrean.api.data.packet.DataActionType
import net.dustrean.api.data.packet.DataObjectPacket
import net.dustrean.api.data.packet.cache.DataCacheActionType
import net.dustrean.api.data.packet.cache.DataCachePacket
import net.dustrean.api.event.CoreEvent
import net.dustrean.api.event.EventType
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.redis.IRedisConnection
import net.dustrean.api.redis.codec.GsonCodec
import net.dustrean.api.redis.codec.JsonJacksonKotlinCodec
import org.slf4j.LoggerFactory
import reactor.core.publisher.whenComplete
import java.util.*
import kotlin.NoSuchElementException

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
                f?.getAnnotation(Expose::class.java)?.serialize == false

            override fun shouldSkipClass(clazz: Class<*>?): Boolean = false

        }).addSerializationExclusionStrategy(object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes?): Boolean =
                f?.getAnnotation(Expose::class.java)?.deserialize == false

            override fun shouldSkipClass(clazz: Class<*>?): Boolean = false
        }).disableHtmlEscaping().create()
    }

    private val cacheScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        MANAGERS[prefix] = this
        ICoreAPI.INSTANCE.getPacketManager().registerPacket(DataCachePacket())
        ICoreAPI.INSTANCE.getPacketManager().registerPacket(DataObjectPacket())
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
                cacheScope.launch {
                    val networkComponents =
                        cachedObject.getCacheHandler().getCacheNetworkComponents().toMutableSet()
                    networkComponents.addAll(cachedObject.getCacheHandler().getCurrentNetworkComponents())
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

    override suspend fun getObject(identifier: UUID): T {
        val cache: T? = getCache(identifier)
        if (cache != null) {
            return cache
        }
        val key = "$prefix:$identifier"
        val bucket = connection.getRedissonClient().getBucket<T>(key)
        if (!bucket.isExists) throw NoSuchElementException("Object with identifier $identifier does not exist!")
        val dataObject = bucket.get()
        if (dataObject.getValidator() == null || (dataObject.getValidator()?.isValid() == true)) {
            cachedObjects[dataObject.getIdentifier()] = dataObject
            sendPacket(
                identifier,
                DataCacheActionType.ADDED,
                dataObject.getCacheHandler().getCacheNetworkComponents()
            )
        }
        return dataObject
    }

    override suspend fun createObject(dataObject: T): T {
        val key = "$prefix:${dataObject.getIdentifier()}"
        val bucket = connection.getRedissonClient().getBucket<T>(key)
        if (existsObject(dataObject.getIdentifier())) throw IllegalArgumentException("Object with identifier ${dataObject.getIdentifier()} already exists!")
        bucket.set(dataObject)
        if (dataObject.getValidator() == null || (dataObject.getValidator()?.isValid() == true)) {
            cachedObjects[dataObject.getIdentifier()] = dataObject
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
        return objectMapper.toJson(dataObject)
    }

    fun deserialize(json: String): T {
        val dataObject = objectMapper.fromJson(json, implClass)
        if (dataObject.getValidator() == null || (dataObject.getValidator()?.isValid() == true)) {
            cachedObjects[dataObject.getIdentifier()] = dataObject
            cacheScope.launch {
                sendPacket(
                    dataObject.getIdentifier(),
                    DataCacheActionType.ADDED,
                    dataObject.getCacheHandler().getCacheNetworkComponents()
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