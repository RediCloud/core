package net.dustrean.api.redis

import kotlinx.serialization.Serializable

@Serializable
data class RedisCredentials (
    val host: String,
    val port: Int,
    val password: String,
    val database: Int
)