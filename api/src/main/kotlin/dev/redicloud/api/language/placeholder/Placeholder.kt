package dev.redicloud.api.language.placeholder

import dev.redicloud.api.language.placeholder.collection.PlaceholderCollection
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class Placeholder(
    private val key: String,
    val value: suspend () -> String,
    val cacheDuration: Duration = 3.seconds,
    val parsed: Boolean = true
) {
    private var cacheTime: Long = 1L
    private var cachedValue: String = ""
    var collection: PlaceholderCollection? = null

    fun getKey(): String =
        if (collection != null) "${collection!!.prefix}_$key" else key

    suspend fun parse(): TagResolver {
        if (System.currentTimeMillis() - cacheTime > cacheDuration.inWholeMilliseconds) {
            cachedValue = value()
            cacheTime = System.currentTimeMillis()
        }
        val placeholderKey = getKey()
        return if(parsed) {
            Placeholder.parsed(placeholderKey, cachedValue)
        } else {
            Placeholder.unparsed(placeholderKey, cachedValue)
        }
    }

}