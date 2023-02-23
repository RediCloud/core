package dev.redicloud.api.network

import java.io.Serializable
import java.util.*

class NetworkComponentInfo() : Serializable {
    lateinit var identifier: UUID
    lateinit var type: NetworkComponentType

    constructor(type: NetworkComponentType, identifier: UUID) : this() {
        this.type = type
        this.identifier = identifier
    }

    fun getKey(): String = type.prefix + identifier.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as NetworkComponentInfo
        return identifier == that.identifier && type === that.type
    }

    override fun hashCode(): Int {
        var result = identifier.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}