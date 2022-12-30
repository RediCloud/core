package net.dustrean.api.language.placeholder.collection

import net.dustrean.api.language.placeholder.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

open class PlaceholderCollection(
    val prefix: String = ""
) {

    private val placeholders = mutableListOf<Placeholder>()

    fun add(placeholder: Placeholder) {
        if (placeholder.collection != null) {
            throw IllegalArgumentException("Placeholder already belongs to a collection")
        }
        placeholder.collection = this
        placeholders.add(placeholder)
    }

    fun remove(placeholder: Placeholder) {
        placeholder.collection = null
        placeholders.remove(placeholder)
    }

    fun clear() {
        placeholders.forEach { it.collection = null }
        placeholders.clear()
    }

    suspend fun parse(): List<TagResolver> {
        val tagResolvers = mutableListOf<TagResolver>()
        placeholders.forEach { tagResolvers.add(it.parse()) }
        return tagResolvers
    }

    fun copy(prefix: String): PlaceholderCollection
        = PlaceholderCollection(prefix).also {
            it.placeholders.addAll(placeholders)
        }

}