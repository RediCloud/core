package com.dustrean.api.packet

abstract class Packet(
    var packetData: PacketData = PacketData()
){
    abstract fun received()

}