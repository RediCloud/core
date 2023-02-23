package dev.redicloud.api.utils.extension

import java.nio.charset.StandardCharsets
import java.util.*

fun UUID.isCrackedName(username: String): Boolean =
    UUID.nameUUIDFromBytes(("OfflinePlayer:$username").toByteArray(StandardCharsets.UTF_8)).equals(this)

fun UUID.isPremium(username: String): Boolean =
    !isCrackedName(username)