import com.dustrean.api.network.NetworkComponentInfo
import com.dustrean.api.network.NetworkComponentType
import com.dustrean.api.packet.PacketManager
import com.dustrean.api.redis.RedisConnection
import com.dustrean.api.redis.RedisCredentials
import org.redisson.api.listener.MessageListener
import packet.TestPacket
import java.util.*

fun main(args: Array<String>){


    val networkComponentInfo1 = NetworkComponentInfo(NetworkComponentType.STANDALONE, UUID.fromString("75dfe2a8-6aa7-11ed-a1eb-0242ac120002"))
    val networkComponentInfo2 = NetworkComponentInfo(NetworkComponentType.STANDALONE, UUID.fromString("85dfe2a8-6aa7-11ed-a1eb-0242ac120002"))

    println("Network info: $networkComponentInfo2")

    val credentials = RedisCredentials("168.119.60.230", 6379, "sOmE_sEcUrE_pAsS")
    val connection = RedisConnection(credentials)
    connection.connect()

    println("Connected to redis")

    val packetManager = PacketManager(networkComponentInfo2, connection)

    packetManager.registerPacket(TestPacket())

    println("Waiting for packets...")
}