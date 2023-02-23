package dev.redicloud.api.config

import java.io.Serializable

interface IConfig : Serializable {
    val key: String
}

