package net.dustrean.api.data

import net.dustrean.api.tasks.futures.FutureAction
import java.util.*

interface IDataManager<T : IDataObject> {

    fun isCached(identifier: UUID): Boolean

    fun getCache(identifier: UUID): T?

    fun getCache(): List<T>

    fun getObject(identifier: UUID): FutureAction<T>

    fun unregisterCache()

    fun createObject(dataObject: T) : FutureAction<T>

    fun updateObject(dataObject: T) : FutureAction<T>

    fun deleteObject(dataObject: T) : FutureAction<Unit>

    fun getDataPrefix(): String

}