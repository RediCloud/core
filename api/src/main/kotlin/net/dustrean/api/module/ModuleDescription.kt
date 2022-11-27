package net.dustrean.api.module

import net.dustrean.api.network.NetworkComponentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.io.File

@Serializable
data class ModuleDescription (
    val name: String,
    val description: String,
    val version: String,
    val mainClasses: HashMap<NetworkComponentType, String>,

    @Transient val file: File = null!!, // This will be set by the module manager
)