package net.dustrean.api.packet

import java.io.Serializable

abstract class Packet(
    var packetData: PacketData = PacketData()
) : Serializable {
    abstract fun received()
}