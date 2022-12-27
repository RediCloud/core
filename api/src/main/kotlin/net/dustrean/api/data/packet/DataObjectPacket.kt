package net.dustrean.api.data.packet

import net.dustrean.api.ICoreAPI
import net.dustrean.api.data.AbstractDataManager
import net.dustrean.api.data.AbstractDataObject
import net.dustrean.api.packet.Packet
import java.util.*

class DataObjectPacket : Packet() {

    lateinit var type: DataActionType
    lateinit var identifier: UUID
    lateinit var json: String
    lateinit var managerPrefix: String

    override fun received() {
        val manager: AbstractDataManager<out AbstractDataObject> = AbstractDataManager.MANAGERS.getOrElse(managerPrefix) {
            return
        }
        when(type) {
            DataActionType.UPDATE -> {
                val obj = manager.deserialize(json)
                ICoreAPI.INSTANCE.getEventManager().callEvent(
                    manager.getUpdateEvent(obj)
                )
            }
            DataActionType.CREATE -> manager.deserialize(json)
            DataActionType.DELETE -> {
                manager.cachedObjects.remove(identifier)
            }
        }
    }

}