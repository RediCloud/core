package dev.redicloud.api.data

interface ICacheValidator<T : AbstractDataObject> {

    fun isValid(): Boolean

}