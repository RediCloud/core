package com.dustrean.api.packet

import com.dustrean.api.network.INetworkComponentInfo
import com.dustrean.api.packet.coroutines.FutureAction
import com.dustrean.api.packet.response.PacketResponse
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

class PacketData {
    val packetId: UUID = UUID.randomUUID()
    lateinit var senderComponent: INetworkComponentInfo
    val receiverComponent: ArrayList<INetworkComponentInfo> = arrayListOf()
    var allowSenderAsReceiver: Boolean = false
    var responsePacketData: PacketData? = null
    var futureResponse: FutureAction<PacketResponse>? = null

    fun allowSenderAsReceiver() {
        allowSenderAsReceiver = true
    }

    fun isSenderAsReceiverAllowed(): Boolean = allowSenderAsReceiver

    fun addReceiver(receiver: INetworkComponentInfo) = receiverComponent.add(receiver)

    fun removeReceiver(receiver: INetworkComponentInfo) = receiverComponent.remove(receiver)

    fun clearReceiverComponent() = receiverComponent.clear()

    fun waitForResponse(): FutureAction<PacketResponse> {
        futureResponse = FutureAction()
        futureResponse!!.orTimeout(15.seconds)
        return futureResponse!!
    }

    fun hasReceiver(componentInfo: INetworkComponentInfo): Boolean = receiverComponent.contains(componentInfo)
}