package net.dustrean.api.data.packet.cache

import net.dustrean.api.data.AbstractDataManager
import net.dustrean.api.packet.Packet
import java.util.UUID

class DataCachePacket : Packet() {

    lateinit var identifier: UUID
    lateinit var managerPrefix: String
    lateinit var action: DataCacheActionType

    override fun received() {
        if(!AbstractDataManager.MANAGERS.containsKey(managerPrefix)) return
        val manager = AbstractDataManager.MANAGERS[managerPrefix]!!
        val cachedObject = manager.getCache(identifier)
        if(cachedObject == null) return
        when(action) {
            DataCacheActionType.ADDED -> cachedObject.getCacheHandler().currentCached.add(packetData.senderComponent)
            DataCacheActionType.REMOVED -> cachedObject.getCacheHandler().currentCached.remove(packetData.senderComponent)
        }
    }

}