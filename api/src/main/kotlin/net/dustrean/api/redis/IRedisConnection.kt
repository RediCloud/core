package net.dustrean.api.redis

import org.redisson.api.RedissonClient

interface IRedisConnection {

    fun connect()

    fun disconnect()

    fun isConnected(): Boolean

    fun getRedissonClient(): RedissonClient

}