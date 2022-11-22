package com.dustrean.api.module

import com.dustrean.api.network.NetworkComponentType

interface IModuleDescription {

    fun getName(): String
    fun getDescription(): String
    fun getVersion(): String
    fun getModuleClasses(): HashMap<NetworkComponentType, String>

}