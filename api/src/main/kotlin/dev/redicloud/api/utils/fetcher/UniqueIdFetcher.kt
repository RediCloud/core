package dev.redicloud.api.utils.fetcher

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.regex.Pattern
import java.util.stream.Stream

class UniqueIdFetcher {

    companion object {
        private val fetcher = UniqueIdFetcher()

        suspend fun fetchName(uniqueId: UUID, useCache: Boolean = true): String? {
            if (fetcher.nameCache.containsKey(uniqueId) && useCache) {
                return fetcher.nameCache[uniqueId]
            }
            val connection = fetcher.createConnection("https://api.minetools.eu/profile/$uniqueId")
            val reader = BufferedReader(InputStreamReader(connection.inputStream, StandardCharsets.UTF_8))
            val name = fetcher.fetchLinesByName(reader.lines()) ?: return null
            fetcher.nameCache[uniqueId] = name
            fetcher.uniqueIdCache[name] = uniqueId
            fetcher.nameCache[uniqueId] = name
            return name
        }


        suspend fun fetchUniqueId(name: String, useCache: Boolean = true): UUID? {
            val identifierName = name.lowercase()
            if (fetcher.uniqueIdCache.containsKey(identifierName) && useCache) {
                return fetcher.uniqueIdCache[identifierName]
            }
            val connection = fetcher.createConnection("https://api.minetools.eu/uuid/$identifierName")
            val reader = BufferedReader(InputStreamReader(connection.inputStream, StandardCharsets.UTF_8))
            val rawUniqueId = fetcher.fetchLinesByUUID(reader.lines()) ?: return null
            val uniqueId = UUID.fromString(fetcher.pattern.matcher(rawUniqueId).replaceAll("$1-$2-$3-$4-$5"))
            fetcher.uniqueIdCache[identifierName] = uniqueId
            fetcher.nameCache[uniqueId] = name
            return uniqueId
        }

        fun registerCache(uniqueId: UUID, name: String) {
            fetcher.uniqueIdCache[name.lowercase()] = uniqueId
            fetcher.nameCache[uniqueId] = name
        }

    }

    private val uniqueIdCache = mutableMapOf<String, UUID>()
    private val nameCache = mutableMapOf<UUID, String>()
    private val pattern = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})")

    private suspend fun createConnection(url: String): HttpURLConnection =
        withContext(Dispatchers.IO) {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doOutput = false
            connection.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
            )
            connection.connectTimeout = 3000
            connection.readTimeout = 3000
            connection.useCaches = true
            connection.connect()
            return@withContext connection
        }

    private fun fetchLinesByName(stream: Stream<String?>): String? {
        val line = stream.filter { e: String? ->
            e!!.trim { it <= ' ' }.startsWith("\"name\": ")
        }.findFirst().orElse(null)
        return line?.replace("\"".toRegex(), "")
            ?.replace("name: ".toRegex(), "")
            ?.replace(",".toRegex(), "")
            ?.trim { it <= ' ' }
    }


    private fun fetchLinesByUUID(stream: Stream<String?>): String? {
        val line = stream.filter { e: String? ->
            e!!.trim { it <= ' ' }.startsWith("\"id\": ")
        }.findFirst().orElse(null)
        return line?.replace("\"", "")
            ?.replace("id: ", "")
            ?.replace(",", "")
            ?.trim { it <= ' ' }
    }

}