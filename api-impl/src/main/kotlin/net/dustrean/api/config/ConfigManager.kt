package net.dustrean.api.config

import com.google.gson.Gson
import net.dustrean.api.redis.RedisConnection
import net.dustrean.api.tasks.futures.FutureAction

//TODO: Update per packet to other services
class ConfigManager(val redisConnection: RedisConnection) : IConfigManager{

    private val cache = mutableListOf<IConfig>()
    private val gson = Gson()

    override fun <T : IConfig> getConfig(key: String): FutureAction<T> {
        val future = FutureAction<T>()
        if(cache.any { it.key == key }){
            future.complete(cache.first { it.key == key } as T)
            return future
        }
        val bucket = redisConnection.redisClient.getBucket<ConfigData>("config:$key")
        bucket.isExistsAsync.whenComplete { exists, throwable ->
            if(throwable != null){
                future.completeExceptionally(throwable)
                return@whenComplete
            }
            if(!exists){
                future.completeExceptionally(NoSuchElementException("Config with key $key does not exist"))
                return@whenComplete
            }
            bucket.async.whenComplete { configData, throwable1 ->
                if(throwable1 != null){
                    future.completeExceptionally(throwable1)
                    return@whenComplete
                }
                try {
                    val config = gson.fromJson(configData.json, Class.forName(configData.clazz))
                    cache.add(config as IConfig)
                    future.complete(config as T)
                }catch (e: Exception){
                    future.completeExceptionally(e)
                }
            }
        }

        return future
    }

    override fun <T : IConfig> createConfig(config: T): FutureAction<T> {
        val future = FutureAction<T>()
        val bucket = redisConnection.redisClient.getBucket<ConfigData>("config:${config.key}")
        bucket.isExistsAsync.whenComplete { exists, throwable ->
            if(throwable != null){
                future.completeExceptionally(throwable)
                return@whenComplete
            }
            if(exists){
                future.completeExceptionally(IllegalArgumentException("Config with key ${config.key} already exists"))
                return@whenComplete
            }
            val configData = ConfigData(gson.toJson(config), config.javaClass.name)
            bucket.setAsync(configData).whenComplete { _, throwable1 ->
                if(throwable1 != null){
                    future.completeExceptionally(throwable1)
                    return@whenComplete
                }
                cache.add(config)
                future.complete(config)
            }
        }
        return future
    }

    override fun deleteConfig(key: String): FutureAction<Unit> {
        val future = FutureAction<Unit>()
        val bucket = redisConnection.redisClient.getBucket<ConfigData>("config:$key")
        bucket.isExistsAsync.whenComplete { exists, throwable ->
            if(throwable != null){
                future.completeExceptionally(throwable)
                return@whenComplete
            }
            if(!exists){
                future.completeExceptionally(NoSuchElementException("Config with key $key does not exist"))
                return@whenComplete
            }
            bucket.deleteAsync().whenComplete { _, throwable1 ->
                if(throwable1 != null){
                    future.completeExceptionally(throwable1)
                    return@whenComplete
                }
                cache.removeIf { it.key == key }
                future.complete(Unit)
            }
        }
        return future
    }

    override fun deleteConfig(config: IConfig): FutureAction<Unit> {
        return deleteConfig(config.key)
    }

    override fun <T : IConfig> saveConfig(config: T): FutureAction<Unit> {
        val future = FutureAction<Unit>()
        val bucket = redisConnection.redisClient.getBucket<ConfigData>("config:${config.key}")
        bucket.isExistsAsync.whenComplete{ exists, throwable ->
            if(throwable != null){
                future.completeExceptionally(throwable)
                return@whenComplete
            }
            if(!exists){
                future.completeExceptionally(NoSuchElementException("Config with key ${config.key} does not exist"))
                return@whenComplete
            }
            val configData = ConfigData(gson.toJson(config), config.javaClass.name)
            bucket.setAsync(configData).whenComplete { _, throwable1 ->
                if(throwable1 != null){
                    future.completeExceptionally(throwable1)
                    return@whenComplete
                }
                cache.add(config)
                future.complete(Unit)
            }
        }
        return future
    }

}