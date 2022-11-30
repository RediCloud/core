package net.dustrean.api.redis

import kotlinx.serialization.Serializable

@Serializable
data class RedisConfiguration(
    val subscriptionConnectionMinimumIdleSize: Int = 1,
    val subscriptionConnectionPoolSize: Int = 50,
    val connectionMinimumIdleSize: Int = 24,
    val connectionPoolSize: Int = 64,
    val credentials: RedisCredentials = RedisCredentials("localhost", 6379, "password", 0),
)