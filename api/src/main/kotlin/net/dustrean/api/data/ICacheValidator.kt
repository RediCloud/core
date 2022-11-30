package net.dustrean.api.data

interface ICacheValidator<T : IDataObject> {

    fun isValid(): Boolean

}