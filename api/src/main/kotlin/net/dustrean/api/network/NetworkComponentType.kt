package net.dustrean.api.network

import java.io.Serializable

enum class NetworkComponentType(val prefix: String) : Serializable {
    PAPER("paper@"),
    VELOCITY("velocity@"),
    MINESTOM("minestom@"),
    STANDALONE("standalone@")
}