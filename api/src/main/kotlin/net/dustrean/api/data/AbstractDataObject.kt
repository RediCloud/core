package net.dustrean.api.data

import java.io.Serializable
import java.util.*

abstract class AbstractDataObject() : Serializable {

    abstract fun update()

    abstract fun getIdentifier(): UUID

    abstract fun getCacheHandler(): AbstractCacheHandler

    abstract fun getValidator(): ICacheValidator<AbstractDataObject>?

}