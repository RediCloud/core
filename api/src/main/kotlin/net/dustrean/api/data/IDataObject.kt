package net.dustrean.api.data

import java.io.Serializable
import java.util.*

interface IDataObject : Serializable {

    fun update()

    fun getIdentifier(): UUID

    fun getCacheHandler(): AbstractCacheHandler<IDataObject>

    fun getValidator(): ICacheValidator<IDataObject>?

}