package net.dustrean.api.utils.fetcher

import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.URL
import java.util.*

class TextureFetcher {

    companion object {
        private val fetcher = TextureFetcher()

        suspend fun fetchTexture(uniqueId: UUID, useCache: Boolean = true): String = fetcher.fetchTexture(uniqueId)

        fun registerCache(uniqueId: UUID, texture: String) {
            fetcher.textureCache[uniqueId] = texture
        }
    }

    private val textureCache = mutableMapOf<UUID, String>()

    suspend fun fetchTexture(uniqueId: UUID, useCache: Boolean = true): String {
        if (textureCache.containsKey(uniqueId) && useCache) return textureCache[uniqueId]!!
        val url = URL("https://api.minetools.eu/profile/$uniqueId")
        val stream = InputStreamReader(withContext(Dispatchers.IO) {
            url.openConnection().getInputStream()
        })
        val textureUrl = JsonParser
            .parseReader(stream).asJsonObject
            .get("decoded").asJsonObject
            .get("properties").asJsonArray[0]
            .asJsonObject.get("value").asString
        textureCache[uniqueId] = textureUrl
        return textureUrl
    }

}