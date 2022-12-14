package net.dustrean.api.data.packet

import net.dustrean.api.data.AbstractDataManager
import net.dustrean.api.packet.Packet
import java.util.UUID

class DataObjectPacket : Packet() {

    lateinit var type: DataActionType
    lateinit var identifier: UUID
    lateinit var json: String
    lateinit var managerPrefix: String

    override fun received() {
        if(!AbstractDataManager.MANAGERS.containsKey(managerPrefix)) return
        val manager = AbstractDataManager.MANAGERS[managerPrefix]!!
        when(type) {
            DataActionType.UPDATE -> manager.deserialize(identifier, json)
            DataActionType.CREATE -> manager.deserialize(identifier, json)
            DataActionType.DELETE -> {
                manager.parkedObjects.remove(identifier)
                manager.cachedObjects.remove(identifier)
            }
        }
    }

}