package net.dustrean.api.utils

import java.io.File

fun getManageFolder(): File {
    val manageFolder = File(System.getenv("DUSTREAN_MANAGE_FOLDER"))
    if (!manageFolder.exists()) {
        manageFolder.mkdir()
    }
    return manageFolder
}

fun getModuleFolder(): File {
    val moduleFolder = File(getManageFolder(), "modules")
    if (!moduleFolder.exists()) {
        moduleFolder.mkdir()
    }
    return moduleFolder
}

val coreVersion = ""