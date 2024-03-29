package dev.redicloud.api.redis

data class RedisCredentials(
    val host: String,
    val port: Int,
    val password: String,
    val database: Int
)