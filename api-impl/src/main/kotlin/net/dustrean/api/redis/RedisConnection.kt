package net.dustrean.api.redis

import net.dustrean.api.redis.codec.JsonJacksonKotlinCodec
import com.fasterxml.jackson.databind.ObjectMapper
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config

class RedisConnection(
    private val configuration: RedisConfiguration = RedisConfiguration()
): IRedisConnection{
    lateinit var redisClient: RedissonClient
    private val credentials = configuration.credentials

    override fun connect() {
        val config = Config()
        config.useSingleServer()
            .setConnectionPoolSize(configuration.connectionPoolSize)
            .setConnectionMinimumIdleSize(configuration.connectionMinimumIdleSize)
            .setSubscriptionConnectionPoolSize(configuration.subscriptionConnectionPoolSize)
            .setSubscriptionConnectionMinimumIdleSize(configuration.subscriptionConnectionMinimumIdleSize)
            .setAddress("redis://${credentials.host}:${credentials.port}").password = credentials.password

        config.codec = JsonJacksonKotlinCodec(ObjectMapper())

        redisClient = Redisson.create(config)
    }

    override fun disconnect() = redisClient.shutdown()

    override fun isConnected(): Boolean = redisClient.isShutdown
    override fun getRedissonClient(): RedissonClient {
        return redisClient
    }
}