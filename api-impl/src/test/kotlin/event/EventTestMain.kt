import com.dustrean.api.event.EventManager
import com.dustrean.api.network.NetworkComponentInfo
import com.dustrean.api.network.NetworkComponentType
import com.dustrean.api.packet.PacketManager
import com.dustrean.api.redis.RedisConnection
import com.dustrean.api.redis.RedisCredentials
import event.TestEvent
import event.TestListener
import java.util.*

suspend fun main(args: Array<String>) {

    val networkComponentInfo1 = NetworkComponentInfo(NetworkComponentType.STANDALONE, UUID.fromString("75dfe2a8-6aa7-11ed-a1eb-0242ac120002"))

    val credentials = RedisCredentials("168.119.60.230", 6379, "sOmE_sEcUrE_pAsS")
    val connection = RedisConnection(credentials)
    connection.connect()

    val packetManager = PacketManager(networkComponentInfo1, connection)

    val eventManager = EventManager()
    eventManager.registerListener(TestListener())
    eventManager.registerListener(TestListener())

    val event = TestEvent()
    event.message = "Hello World 1!"

    eventManager.callEvent(event)
    event.message = "Hello World 2!"
    eventManager.callEvent(event)

    println("Event called")

}