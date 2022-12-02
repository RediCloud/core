package net.dustrean.api.data

import com.fasterxml.jackson.databind.ObjectMapper
import net.dustrean.api.ICoreAPI
import net.dustrean.api.data.packet.DataActionType
import net.dustrean.api.data.packet.DataObjectPacket
import net.dustrean.api.data.packet.cache.DataCacheActionType
import net.dustrean.api.data.packet.cache.DataCachePacket
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.redis.IRedisConnection
import net.dustrean.api.redis.codec.JsonJacksonKotlinCodec
import net.dustrean.api.tasks.futures.FutureAction
import org.slf4j.LoggerFactory
import java.util.*

abstract class AbstractDataManager<T : IDataObject>(
    private val prefix: String,
    private val connection: IRedisConnection,
    private val implClass: Class<T>
) : IDataManager<T> {

    companion object {
        val MANAGERS = mutableMapOf<String, AbstractDataManager<out IDataObject>>()
        private val codec = JsonJacksonKotlinCodec(ObjectMapper())
    }

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

    fun sendPacket(identifier: UUID, action: DataCacheActionType, components: List<NetworkComponentInfo>) {
        val packet = DataCachePacket()
        packet.identifier = identifier
        packet.managerPrefix = prefix
        packet.action = action
        packet.packetData.receiverComponent.addAll(components)
        ICoreAPI.INSTANCE.getPacketManager().sendPacket(packet)
    }

    fun sendPacket(dataObject: T, action: DataActionType): FutureAction<Unit> {
        val future = FutureAction<Unit>()
        val packet = DataObjectPacket()
        packet.json = serialize(dataObject)
        packet.type = action
        packet.identifier = dataObject.getIdentifier()
        packet.managerPrefix = prefix
        dataObject.getCacheHandler().getCacheNetworkComponents().whenComplete { components, throwable ->
            if (throwable != null) {
                future.completeExceptionally(throwable)
                return@whenComplete
            }
            packet.packetData.receiverComponent.addAll(components)
            ICoreAPI.INSTANCE.getPacketManager().sendPacket(packet)
            future.complete(Unit)
        }
        return future
    }

    override fun getCache(identifier: UUID): T? {
        if (cachedObjects.containsKey(identifier)) {
            val cachedObject = cachedObjects[identifier]!!
            if (cachedObject.getValidator() != null) {
                if (cachedObject.getValidator()!!.isValid()) return cachedObject
                cachedObjects.remove(identifier)
                parkedObjects[identifier] = cachedObject

                cachedObject.getCacheHandler().getCacheNetworkComponents().whenComplete { components, throwable ->
                    if (throwable != null) {
                        logger.error("Error while getting cache network components for object $identifier", throwable)
                        return@whenComplete
                    }
                    sendPacket(identifier, DataCacheActionType.REMOVED, components)
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

                    parkedObject.getCacheHandler().getCacheNetworkComponents().whenComplete { components, throwable ->
                        if (throwable != null) {
                            logger.error(
                                "Error while getting cache network components for object $identifier", throwable
                            )
                            return@whenComplete
                        }
                        sendPacket(identifier, DataCacheActionType.ADDED, components)
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

            cachedObject.getCacheHandler().getCacheNetworkComponents().whenComplete { components, throwable ->
                if (throwable != null) {
                    logger.error(
                        "Error while getting cache network components for object ${cachedObject.getIdentifier()}",
                        throwable
                    )
                    return@whenComplete
                }
                sendPacket(cachedObject.getIdentifier(), DataCacheActionType.REMOVED, components)
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

    override fun getObject(identifier: UUID): FutureAction<T> {
        val future = FutureAction<T>()
        val cache: T? = getCache(identifier)
        if (cache != null) {
            future.complete(cache)
            return future
        }
        val key = "$prefix@$identifier"
        val bucket = connection.getRedissonClient().getBucket<T>(key)
        bucket.async.whenComplete { dataObject, throwable ->
            if (throwable != null) {
                future.completeExceptionally(throwable)
                return@whenComplete
            }
            future.complete(dataObject)
        }
        return future
    }

    override fun createObject(dataObject: T): FutureAction<T> {
        val future = FutureAction<T>()
        val key = "$prefix@${dataObject.getIdentifier()}"
        val bucket = connection.getRedissonClient().getBucket<T>(key)
        bucket.setAsync(dataObject).whenComplete { _, throwable ->
            if (throwable != null) {
                future.completeExceptionally(throwable)
            }
            var updateCacheList = false
            if (dataObject.getValidator() == null || (dataObject.getValidator()?.isValid() == true)) {
                updateCacheList = true
                cachedObjects[dataObject.getIdentifier()] = dataObject
            } else {
                parkedObjects[dataObject.getIdentifier()] = dataObject
            }
            sendPacket(dataObject, DataActionType.CREATE).whenComplete { _, throwable1 ->
                if (throwable1 != null) {
                    future.completeExceptionally(throwable1)
                    return@whenComplete
                }
                future.complete(dataObject)
            }
        }
        return future
    }

    override fun updateObject(dataObject: T): FutureAction<T> {
        val future = FutureAction<T>()
        val key = "$prefix@${dataObject.getIdentifier()}"
        val bucket = connection.getRedissonClient().getBucket<T>(key)
        bucket.isExistsAsync.whenComplete { exists, throwable ->
            if (throwable != null) {
                future.completeExceptionally(throwable)
            }
            if (!exists) {
                future.completeExceptionally(NoSuchElementException("Object with identifier ${dataObject.getIdentifier()} does not exist"))
            }
            bucket.setAsync(dataObject).whenComplete { _, throwable1 ->
                if (throwable1 != null) {
                    future.completeExceptionally(throwable1)
                }
                var updateCacheList = false

                if (dataObject.getValidator() == null || (dataObject.getValidator()?.isValid() == true)) {
                    updateCacheList = cachedObjects.containsKey(dataObject.getIdentifier())
                    cachedObjects[dataObject.getIdentifier()] = dataObject
                    parkedObjects.remove(dataObject.getIdentifier())
                    updateCacheList = true
                } else {
                    parkedObjects[dataObject.getIdentifier()] = dataObject
                }
                sendPacket(dataObject, DataActionType.UPDATE).whenComplete { _, throwable2 ->
                    if (throwable2 != null) {
                        future.completeExceptionally(throwable2)
                        return@whenComplete
                    }
                    future.complete(dataObject)
                }
            }
        }
        return future
    }

    override fun deleteObject(dataObject: T): FutureAction<Unit> {
        val future = FutureAction<Unit>()
        val key = "$prefix@${dataObject.getIdentifier()}"
        val bucket = connection.getRedissonClient().getBucket<T>(key)
        bucket.isExistsAsync.whenComplete { exists, throwable ->
            if (throwable != null) {
                future.completeExceptionally(throwable)
            }
            if (!exists) {
                future.completeExceptionally(NoSuchElementException("Object with identifier ${dataObject.getIdentifier()} does not exist"))
            }
            bucket.deleteAsync().whenComplete { _, throwable1 ->
                if (throwable1 != null) {
                    future.completeExceptionally(throwable1)
                } else {
                    cachedObjects.remove(dataObject.getIdentifier())
                    parkedObjects.remove(dataObject.getIdentifier())
                    sendPacket(dataObject, DataActionType.DELETE).whenComplete { _, throwable2 ->
                        if (throwable2 != null) {
                            future.completeExceptionally(throwable2)
                            return@whenComplete
                        }
                        future.complete(Unit)
                    }
                }
            }
        }
        return future
    }

    fun serialize(dataObject: T): String {
        return codec.objectMapper.writeValueAsString(dataObject)
    }

    fun deserialize(identifier: UUID?, json: String): T {
        if (identifier != null) {
            var dataObject: T? = null
            if (cachedObjects.containsKey(identifier)) dataObject = cachedObjects[identifier]
            if (parkedObjects.containsKey(identifier)) dataObject = parkedObjects[identifier]
            if (dataObject != null) {
                codec.objectMapper.readerForUpdating(dataObject).readValue<T>(json)
                return dataObject
            }
        }
        return codec.objectMapper.readValue(json, implClass)
    }

    override fun getDataPrefix(): String {
        return prefix
    }

}