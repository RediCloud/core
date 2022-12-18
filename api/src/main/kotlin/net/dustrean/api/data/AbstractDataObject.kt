package net.dustrean.api.data

import net.dustrean.api.tasks.futures.FutureAction
import java.io.Serializable
import java.util.*

abstract class AbstractDataObject() : Serializable {

    abstract fun update(): FutureAction<out AbstractDataObject>

    abstract fun getIdentifier(): UUID

    abstract fun getCacheHandler(): AbstractCacheHandler<AbstractDataObject>

    abstract fun getValidator(): ICacheValidator<AbstractDataObject>?

}