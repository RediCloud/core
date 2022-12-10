package net.dustrean.api.redis


data class RedisConfiguration(
    val subscriptionConnectionMinimumIdleSize: Int = 1,
    val subscriptionConnectionPoolSize: Int = 50,
    val connectionMinimumIdleSize: Int = 24,
    val connectionPoolSize: Int = 64,
    val credentials: RedisCredentials = RedisCredentials(
        System.getenv().getOrDefault("redis_hostname", "localhost"),
        System.getenv().getOrDefault("redis_port", "6379").toInt(),
        System.getenv().getOrDefault("redis_password", "password"),
        System.getenv().getOrDefault("redis_database_id", "0").toInt()
    )
)