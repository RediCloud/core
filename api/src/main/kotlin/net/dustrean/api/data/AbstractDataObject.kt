package net.dustrean.api.data

import java.io.Serializable
import java.util.*

abstract class AbstractDataObject : Serializable {

    abstract suspend fun update(): AbstractDataObject

    abstract val identifier: UUID

    abstract val cacheHandler: AbstractCacheHandler

    abstract val validator: ICacheValidator<AbstractDataObject>?

}