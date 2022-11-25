package com.dustrean.api.module

import com.dustrean.api.network.NetworkComponentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.io.File

@Serializable
class ModuleDescription {
    lateinit var name: String
    lateinit var description: String
    lateinit var version: String
    @Transient
    lateinit var file: File
    lateinit var mainClasses: HashMap<NetworkComponentType, String>
}