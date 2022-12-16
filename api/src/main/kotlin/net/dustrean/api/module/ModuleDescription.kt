package net.dustrean.api.module

import net.dustrean.api.network.NetworkComponentType

data class ModuleDescription(
    val name: String,
    val description: String,
    val version: String,
    val mainClasses: Map<NetworkComponentType, String>
)