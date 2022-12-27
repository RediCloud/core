package net.dustrean.api.config

import java.io.Serializable

interface IConfig : Serializable {
    val key: String
}

