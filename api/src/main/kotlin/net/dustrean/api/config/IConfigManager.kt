package net.dustrean.api.config

import net.dustrean.api.tasks.futures.FutureAction


interface IConfigManager {

    fun <T: IConfig> getConfig(key: String): FutureAction<T>

    fun <T: IConfig> createConfig(config: T): FutureAction<T>

    fun deleteConfig(key: String): FutureAction<Unit>
    fun deleteConfig(config: IConfig): FutureAction<Unit>

    fun <T: IConfig> saveConfig(config: T): FutureAction<Unit>

}