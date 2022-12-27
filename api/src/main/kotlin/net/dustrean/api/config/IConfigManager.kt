package net.dustrean.api.config

interface IConfigManager {

    suspend fun <T : IConfig> getConfig(key: String): T
    suspend fun <T : IConfig> getConfigOrPut(key: String, default: () -> T): T

    suspend fun <T : IConfig> createConfig(config: T): T

    suspend fun deleteConfig(key: String)
    suspend fun deleteConfig(config: IConfig)

    suspend fun <T : IConfig> saveConfig(config: T)

    suspend fun exists(key: String): Boolean
    suspend fun exists(config: IConfig): Boolean

}