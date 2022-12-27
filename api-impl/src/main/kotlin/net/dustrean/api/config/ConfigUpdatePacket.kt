package net.dustrean.api.config

import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.packet.Packet

class ConfigUpdatePacket : Packet() {

    lateinit var key: String
    lateinit var configData: ConfigData
    var delete = false

    override fun received() {
        ICoreAPI.getInstance<CoreAPI>().getConfigManager().readUpdate(key, configData, delete)
    }

}