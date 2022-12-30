package net.dustrean.api.language.placeholder

import net.dustrean.api.language.component.LanguageComponentBuilder
import net.dustrean.api.language.placeholder.collection.PlaceholderCollection
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

class PlaceholderProvider<T>(private val builder: LanguageComponentBuilder<T>) {

    private val placeholders = mutableListOf<Placeholder>()
    private val placeholderCollections = mutableListOf<PlaceholderCollection>()

    fun addPlaceholder(placeholder: Placeholder) = apply { placeholders.add(placeholder) }
    fun addPlaceholderCollection(collection: PlaceholderCollection) = apply {
        placeholderCollections.add(collection)
    }

    fun addStaticPlaceholder(key: String, value: String) = apply {
        placeholders.add(Placeholder(key, value = { value }))
    }

    fun builder() = builder

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