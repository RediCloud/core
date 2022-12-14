package net.dustrean.api.redis


data class RedisConfiguration(
    val subscriptionConnectionMinimumIdleSize: Int = 1,
    val subscriptionConnectionPoolSize: Int = 50,
    val connectionMinimumIdleSize: Int = 24,
    val connectionPoolSize: Int = 64,
    val credentials: RedisCredentials = RedisCredentials(
        System.getenv().getOrDefault("REDIS_HOSTNAME", "localhost"),
        System.getenv().getOrDefault("REDIS_PORT", "6379").toInt(),
        System.getenv().getOrDefault("REDIS_PASSWORD", "password"),
        System.getenv().getOrDefault("REDIS_DATABASE_ID", "0").toInt()
    )
)