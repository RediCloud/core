package net.dustrean.api.module

import net.dustrean.api.network.NetworkComponentType
import java.io.File
import com.google.gson.annotations.Expose

data class ModuleDescription(
    val name: String,
    val description: String,
    val version: String,
    val mainClasses: HashMap<NetworkComponentType, String>,

    @Expose(serialize = false, deserialize = false) val file: File = null!!, // This will be set by the module manager
)