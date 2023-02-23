package dev.redicloud.api.data.packet

import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.data.AbstractDataManager
import dev.redicloud.api.data.AbstractDataObject
import dev.redicloud.api.packet.Packet
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
                ICoreAPI.INSTANCE.eventManager.callEvent(
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