@file:JvmName("StringUtils")

package net.dustrean.api.utils.extension

/**
 * @author vironlab.eu
 *
 * Remove once SteinGaming rewrote vexention
 */

import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

fun String.isCrackedName(uniqueId: UUID): Boolean =
    UUID.nameUUIDFromBytes(("OfflinePlayer:$this").toByteArray(StandardCharsets.UTF_8)).equals(uniqueId)

fun String.isPremiumName(uniqueId: UUID): Boolean =
    !isCrackedName(uniqueId)

fun String.isClass(): Boolean {
    return try {
        Class.forName(this)
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Check if the String is an Integer
 */
fun String.isInt(): Boolean {
    return try {
        this.toInt()
        true
    } catch (e: Exception) {
        false
    }
}

fun String.isLong(): Boolean {
    return try {
        this.toLong()
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Check if the String is a Boolean
 */
fun String.isBoolean(): Boolean {
    return try {
        this.toBoolean()
        true
    } catch (e: Exception) {
        false
    }
}

fun String.containsIgnoreCase(searchQuery: String): Boolean {
    return this.lowercase(Locale.getDefault()).contains(searchQuery.lowercase(Locale.getDefault()))
}

/**
 * Check if the String is an URL
 */
fun String.isUrl(): Boolean {
    return try {
        URL(this)
        true
    } catch (e: Exception) {
        false
    }
}

fun String.isUUID(): Boolean {
    return try {
        this.toUUID()
        true
    } catch (e: Exception) {
        false
    }
}

fun String.toUUID(): UUID {
    return if (contains("-")) {
        UUID.fromString(this)
    } else {
        var uuid = ""
        for (i in 0..31) {
            uuid += this[i]
            if (i == 7 || i == 11 || i == 15 || i == 19) {
                uuid = "$uuid-"
            }
        }
        UUID.fromString(uuid)
    }
}