package net.dustrean.api.language.placeholder

import net.dustrean.api.language.placeholder.collection.PlaceholderCollection
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import java.util.UUID

class PlaceholderProvider() {

    private val placeholders = mutableListOf<Placeholder>()
    private val placeholderCollections = mutableListOf<PlaceholderCollection>()

    fun addPlaceholder(placeholder: Placeholder) {
        placeholders.add(placeholder)
    }

    fun addPlaceholderCollection(collection: PlaceholderCollection) {
        placeholderCollections.add(collection)
    }

    suspend fun parse(uniqueId: UUID, text: String): List<TagResolver> {
        val tagResolvers = mutableListOf<TagResolver>()
        placeholders.forEach {
            if(".*${it.getKey().lowercase()}.*".toRegex().matches(text.lowercase()))
                tagResolvers.add(it.parse(uniqueId))
        }
        placeholderCollections.forEach { tagResolvers.addAll(it.parse(uniqueId)) }
        return tagResolvers
    }

}