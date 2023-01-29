package net.dustrean.api.config

import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.packet.Packet
import net.dustrean.api.utils.extension.JsonObjectData

class ConfigUpdatePacket : Packet() {

    lateinit var key: String
    lateinit var configData: JsonObjectData
    var delete = false

    override fun received() {
        ICoreAPI.getInstance<CoreAPI>().configManager.readUpdate(key, configData, delete)
    }

}