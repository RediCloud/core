package dev.redicloud.api.utils

import java.io.File

fun getManageFolder(): File {
    val manageFolder = File(System.getenv("REDI_CLOUD_MANAGE_FOLDER"))
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