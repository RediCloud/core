package net.dustrean.api.redis

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dustrean.api.utils.getManageFolder
import java.io.File

@Serializable
data class RedisConfiguration(
    val subscriptionConnectionMinimumIdleSize: Int = 1,
    val subscriptionConnectionPoolSize: Int = 50,
    val connectionMinimumIdleSize: Int = 24,
    val connectionPoolSize: Int = 64,
    val credentials: RedisCredentials = RedisCredentials("localhost", 6379, "password", 0),
)

private fun loadConfiguration(): RedisConfiguration {
    val folder = getManageFolder()
    val file = File(folder, "redis.json")
    return if (file.exists()) {
        Json.decodeFromString(file.readText())
    } else {
        RedisConfiguration().also {
            file.writeText(Json.encodeToString(it))
        }
    }
}
