package com.dustrean.api.redis

import com.dustrean.api.redis.codec.JsonJacksonKotlinCodec
import com.fasterxml.jackson.databind.ObjectMapper
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config

class RedisConnection(
    val credentials: RedisCredentials,
    val configuration: RedisConfiguration = RedisConfiguration()
) {
    lateinit var redisClient: RedissonClient

    fun connect() {
        val config = Config()
        config.useSingleServer().setConnectionPoolSize(configuration.connectionPoolSize)
            .setConnectionMinimumIdleSize(configuration.connectionMinimumIdleSize)
            .setAddress("redis://${credentials.host}:${credentials.port}").setPassword(credentials.password)

        config.codec = JsonJacksonKotlinCodec(ObjectMapper())

        redisClient = Redisson.create(config)
    }

    fun disconnect() = redisClient.shutdown()

    fun isConnected(): Boolean = redisClient.isShutdown
}