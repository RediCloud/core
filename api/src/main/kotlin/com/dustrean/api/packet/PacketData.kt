package com.dustrean.api.packet

import com.dustrean.api.network.NetworkComponentInfo
import com.dustrean.api.packet.coroutines.FutureAction
import com.dustrean.api.packet.response.PacketResponse
import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

class PacketData : Serializable{
    val packetId: UUID = UUID.randomUUID()
    lateinit var senderComponent: NetworkComponentInfo
    val receiverComponent: ArrayList<NetworkComponentInfo> = arrayListOf()
    var allowSenderAsReceiver: Boolean = false
    var responsePacketData: PacketData? = null
    @JsonIgnore
    var futureResponse: FutureAction<PacketResponse>? = null

    fun allowSenderAsReceiver() {
        allowSenderAsReceiver = true
    }

    fun isSenderAsReceiverAllowed(): Boolean = allowSenderAsReceiver

    fun addReceiver(receiver: NetworkComponentInfo) = receiverComponent.add(receiver)

    fun removeReceiver(receiver: NetworkComponentInfo) = receiverComponent.remove(receiver)

    fun clearReceiverComponent() = receiverComponent.clear()

    fun waitForResponse(): FutureAction<PacketResponse> {
        futureResponse = FutureAction()
        futureResponse!!.orTimeout(15.seconds)
        return futureResponse!!
    }

    fun hasReceiver(componentInfo: NetworkComponentInfo): Boolean = receiverComponent.contains(componentInfo)
}