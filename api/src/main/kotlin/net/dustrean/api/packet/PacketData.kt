package net.dustrean.api.packet

import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.packet.response.PacketResponse
import com.google.gson.annotations.Expose
import kotlinx.coroutines.CompletableDeferred
import java.io.Serializable
import java.util.*

class PacketData : Serializable{
    val packetId: UUID = UUID.randomUUID()
    lateinit var senderComponent: NetworkComponentInfo
    val receiverComponent: MutableSet<NetworkComponentInfo> = mutableSetOf()
    var allowSenderAsReceiver: Boolean = false
    var responsePacketData: PacketData? = null

    @Expose(serialize = false, deserialize = false)
    var futureResponse: CompletableDeferred<PacketResponse>? = null

    fun allowSenderAsReceiver() {
        allowSenderAsReceiver = true
    }

    fun isSenderAsReceiverAllowed(): Boolean = allowSenderAsReceiver

    fun addReceiver(receiver: NetworkComponentInfo) = receiverComponent.add(receiver)

    fun removeReceiver(receiver: NetworkComponentInfo) = receiverComponent.remove(receiver)

    fun clearReceiverComponent() = receiverComponent.clear()

    fun waitForResponse(): CompletableDeferred<PacketResponse> {
        futureResponse = CompletableDeferred()
        //TODO: timeout 30 sek
        return futureResponse!!
    }

    fun hasReceiver(componentInfo: NetworkComponentInfo): Boolean = receiverComponent.contains(componentInfo)
}