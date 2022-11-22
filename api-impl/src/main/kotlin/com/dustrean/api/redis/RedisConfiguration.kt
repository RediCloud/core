package com.dustrean.api.redis

data class RedisConfiguration(
    val subscriptionConnectionMinimumIdleSize: Int = 1,
    val subscriptionConnectionPoolSize: Int = 50,
    val connectionMinimumIdleSize: Int = 24,
    val connectionPoolSize: Int = 64
)
