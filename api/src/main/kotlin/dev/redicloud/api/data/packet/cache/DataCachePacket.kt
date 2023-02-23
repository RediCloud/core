package dev.redicloud.api.data.packet.cache

import dev.redicloud.api.data.AbstractDataManager
import dev.redicloud.api.packet.Packet
import java.util.*

class DataCachePacket : Packet() {

    lateinit var identifier: UUID
    lateinit var managerPrefix: String
    lateinit var action: DataCacheActionType

    override fun received() {
        if (!AbstractDataManager.MANAGERS.containsKey(managerPrefix)) return
        val manager = AbstractDataManager.MANAGERS[managerPrefix]!!
        val cachedObject = manager.getCache(identifier) ?: return
        when (action) {
            DataCacheActionType.ADDED -> cachedObject.cacheHandler.currentCached.add(packetData.senderComponent)
            DataCacheActionType.REMOVED -> cachedObject.cacheHandler.currentCached.remove(packetData.senderComponent)
        }
    }

}