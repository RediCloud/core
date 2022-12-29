package net.dustrean.api.language

import kotlinx.coroutines.*
import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.language.components.ILanguageComponent
import net.dustrean.api.language.components.LanguageComponent
import net.kyori.adventure.text.Component
import org.redisson.api.RMap
import java.util.*

class LanguageManager(core: CoreAPI) : ILanguageManager {

    companion object {
        const val DEFAULT_LANGUAGE = 0
        const val DEFAULT_PRIMARY_COLOR = "#3ABFF8"
        const val DEFAULT_SECONDARY_COLOR = "#3b82f6"
    }

    private val playerMap: RMap<UUID, PlayerSettings> =
        core.getRedisConnection().getRedissonClient().getMap("langSetting")
    private val messageMap =
        mutableMapOf<Int, RMap<String, String>>(DEFAULT_LANGUAGE to core.getRedisConnection().getRedissonClient().getMap("lang:$DEFAULT_LANGUAGE"))

    // I know that we have object like AbstractDataManager, but I didn't want to use it here for the sake of speed and simplicity
    // Nest hashmaps seem like jank, but it is specifically faster than a single hashmap with a key like "0;messageKey"
    private val messageCache = mutableMapOf<Int, MutableMap<String, String>>()

    // This could be saved as a Triple, but again it's faster to split the string
    private val playerCache = mutableMapOf<UUID, PlayerSettings>()

    private val scope = CoroutineScope(Dispatchers.IO)


    override fun getMessage(
        messageKey: String,
        uuid: UUID,
        vararg placeHolder: Pair<String, Component>,
    ): ILanguageComponent =
        LanguageComponent(messageKey, uuid, *placeHolder)


    override fun getMessageAsync(
        messageKey: String,
        uuid: UUID,
        vararg placeHolder: Pair<String, Component>,
    ): Deferred<ILanguageComponent> =
        scope.async {
            getMessage(messageKey, uuid, *placeHolder)
        }

    override fun getRawMessage(messageKey: String, languageID: Int): String {

        messageCache[languageID]?.get(messageKey)?.let {
            return it
        }

        val langMap = messageMap[languageID] ?: kotlin.run {
            messageMap[languageID] = ICoreAPI.INSTANCE.getRedisConnection().getRedissonClient()
                .getMap("lang:$languageID")
            messageMap[languageID]!!
        }

        return langMap[messageKey] ?: kotlin.run {
            langMap[messageKey] = getRawMessage(messageKey, DEFAULT_LANGUAGE)
            langMap[messageKey]!!
        }
    }

    override fun getRawMessageAsync(messageKey: String, languageID: Int): Deferred<String> =
        scope.async {
            getRawMessage(messageKey, languageID)
        }

    /*
    * Get the language settings of a player
    *
    * @param audience The audience(usually a player) to get the settings from
    *
    * return The language settings as a String in the format "languageID;primaryColor;secondaryColor"
     */
    fun getPlayerSettings(uuid: UUID): PlayerSettings {
        playerCache[uuid]?.let {
            return it
        }

        return playerMap[uuid]
            ?: createPlayerSettings(uuid)
    }

    /*
    * Create the language settings of a player
    * Default settings are languageID = 0, primaryColor = #3ABFF8, secondaryColor = #3b82f6
    *
    * @param audience The audience(usually a player) to create the settings for
    *
    * return The language settings as a String in the format "languageID;primaryColor;secondaryColor"
     */
    private fun createPlayerSettings(
        uuid: UUID,
    ): PlayerSettings {
        val settings = PlayerSettings()
        playerMap[uuid] = settings
        return settings
    }
}