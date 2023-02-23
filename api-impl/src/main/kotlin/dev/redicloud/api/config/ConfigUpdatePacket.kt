package dev.redicloud.api.config

import dev.redicloud.api.CoreAPI
import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.packet.Packet
import dev.redicloud.api.utils.extension.JsonObjectData

class ConfigUpdatePacket : Packet() {

    lateinit var key: String
    lateinit var configData: JsonObjectData
    var delete = false

    override fun received() {
        ICoreAPI.getInstance<CoreAPI>().configManager.readUpdate(key, configData, delete)
    }

}