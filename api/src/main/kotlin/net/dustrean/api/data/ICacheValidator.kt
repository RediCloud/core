package net.dustrean.api.data

interface ICacheValidator<T : AbstractDataObject> {

    fun isValid(): Boolean

}