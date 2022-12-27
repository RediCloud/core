package net.dustrean.api.data.packet.cache

import net.dustrean.api.data.AbstractDataManager
import net.dustrean.api.packet.Packet
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