package net.dustrean.api.language.placeholder

import net.dustrean.api.language.placeholder.collection.PlaceholderCollection
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

class PlaceholderProvider {

    val placeholders = mutableListOf<Placeholder>()
    val placeholderCollections = mutableListOf<PlaceholderCollection>()

    fun addStaticPlaceholder(key: String, value: String) = apply {
        placeholders.add(Placeholder(key, value = { value }))
    }

    suspend fun parse(text: String): TagResolver {
        val tagResolvers = mutableListOf<TagResolver>()
        tagResolvers.add(TagResolver.standard())
        placeholders.forEach {
            if (".*${it.getKey().lowercase()}.*".toRegex().matches(text.lowercase())) tagResolvers.add(it.parse())
        }
        placeholderCollections.forEach { tagResolvers.addAll(it.parse()) }
        return TagResolver.resolver(tagResolvers)
    }

}