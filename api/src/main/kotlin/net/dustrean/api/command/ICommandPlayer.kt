package net.dustrean.api.command

import java.util.*

interface ICommandPlayer {

    val uuid: UUID

    fun hasPermission(permission: String): Boolean
    fun sendMessage(message: String)

    val baseClass: Class<*>
        get() = this::class.java
}