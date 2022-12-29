package net.dustrean.api.language.placeholder

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class Placeholder(
    val key: String,
    val cacheDuration: Duration = 3.seconds,
    val value: () -> Component
) {
    private var cacheTime: Long = 1L
    private var cachedValue: String = ""

    suspend fun parse(): TagResolver{
        val list = mutableListOf<TagResolver>()

    }

}