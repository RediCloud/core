package net.dustrean.api.data

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonMerge
import java.io.Serializable
import java.util.*

abstract class AbstractDataObject() : Serializable {

    abstract fun update()

    abstract fun getIdentifier(): UUID

    @JsonIgnore
    abstract fun getCacheHandler(): AbstractCacheHandler<AbstractDataObject>

    @JsonIgnore
    abstract fun getValidator(): ICacheValidator<AbstractDataObject>?

}