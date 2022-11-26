import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.network.NetworkComponentType
import net.dustrean.api.packet.PacketManager
import net.dustrean.api.redis.RedisConnection
import net.dustrean.api.redis.RedisCredentials
import packet.TestPacket
import packet.TestPingPacket
import packet.TestPingResponsePacket
import java.util.*

fun main(args: Array<String>){

    val networkComponentInfo1 = NetworkComponentInfo(NetworkComponentType.STANDALONE, UUID.fromString("75dfe2a8-6aa7-11ed-a1eb-0242ac120002"))
    val networkComponentInfo2 = NetworkComponentInfo(NetworkComponentType.STANDALONE, UUID.fromString("85dfe2a8-6aa7-11ed-a1eb-0242ac120002"))

    println("Network info: $networkComponentInfo1")

    val credentials = RedisCredentials("168.119.60.230", 6379, "sOmE_sEcUrE_pAsS", 0)
    val connection = RedisConnection(credentials)
    connection.connect()

    println("Connected to redis")

    val packetManager = PacketManager(networkComponentInfo1, connection)

    packetManager.registerPacket(TestPacket())
    packetManager.registerPacket(TestPingPacket())
    packetManager.registerPacket(TestPingResponsePacket())

    val packet = TestPingPacket()
    packet.packetData.receiverComponent.add(networkComponentInfo2)

    packet.start = System.currentTimeMillis()
    packetManager.sendPacket(packet)

    println("Packet sent")
}