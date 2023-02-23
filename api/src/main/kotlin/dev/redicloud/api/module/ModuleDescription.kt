package dev.redicloud.api.module

import dev.redicloud.api.network.NetworkComponentType

data class ModuleDescription(
    val name: String,
    val description: String,
    val version: String,
    val mainClasses: HashMap<NetworkComponentType, String>
)