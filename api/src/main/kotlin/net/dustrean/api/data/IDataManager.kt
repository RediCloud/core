package net.dustrean.api.data

import java.util.*

interface IDataManager<T : AbstractDataObject> {

    fun isCached(identifier: UUID): Boolean

    fun getCache(identifier: UUID): T?

    fun getCache(): List<T>

    fun unregisterCache()

    fun getDataPrefix(): String

    suspend fun getObject(identifier: UUID): T

    suspend fun createObject(dataObject: T): T

    suspend fun updateObject(dataObject: T): T

    suspend fun deleteObject(dataObject: T)

    suspend fun existsObject(identifier: UUID): Boolean


}