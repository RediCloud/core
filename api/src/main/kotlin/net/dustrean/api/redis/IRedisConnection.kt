package net.dustrean.api.redis

interface IRedisConnection {

    fun connect()

    fun disconnect()

    fun isConnected(): Boolean

}