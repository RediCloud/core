package net.dustrean.api.language

import kotlinx.coroutines.*
import net.dustrean.api.ICoreAPI
import net.dustrean.api.language.components.ILangComponent
import net.dustrean.api.language.components.LangComponent
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import org.redisson.api.RMap

object LanguageManger : ILanguageManager {

    private const val DEFAULT_LANGUAGE = 0
    private const val DEFAULT_PRIMARY_COLOR = "#3ABFF8"
    private const val DEFAULT_SECONDARY_COLOR = "#3b82f6"

    private val playerMap: RMap<String, String> =
        ICoreAPI.INSTANCE.getRedisConnection().getRedissonClient().getMap("langSetting")
    private val messageMap =
        HashMap<Int, RMap<String, String>>(ICoreAPI.INSTANCE.getRedisConnection().getRedissonClient().getMap("lang:0"))

    // I know that we have object like AbstractDataManager, but I didn't want to use it here for the sake of speed and simplicity
    // Nest hashmaps seem like jank, but it is specifically faster than a single hashmap with a key like "0;messageKey"
    private val messageCache = HashMap<Int, HashMap<String, String>>()

    // This could be saved as a Triple, but again it's faster to split the string
    private val playerCache = HashMap<String, String>()

    private val scope = CoroutineScope(Dispatchers.IO)

    /*
    * Get a message from the datebase and parse placeholders and colors with miniMessage
    *
    * @param messageKey The key of the message in the database
    * @param audience The audience(usually a player) to get the settings from
    * @param placeHolder The placeholders to replace in the message (<placeholder></placeholder>)
    *
    * @return The parsed message as ILangComponent
    */
    override fun getMessage(
        messageKey: String,
        audience: Audience,
        vararg placeHolder: Pair<String, Component>,
    ): ILangComponent =
        LangComponent(messageKey, audience, *placeHolder)

    /*
    * Get a message from the datebase and parse placeholders and colors with miniMessage
    * This method is async and returns a Deferred<ILangComponent>

    * @param messageKey The key of the message in the database
    * @param audience The audience(usually a player) to get the settings from
    * @param placeHolder The placeholders to replace in the message (<placeholder></placeholder>)
    *
    * @return The parsed message as Deferred<ILangComponent>
     */
    override fun getMessageAsync(
        messageKey: String,
        audience: Audience,
        vararg placeHolder: Pair<String, Component>,
    ): Deferred<ILangComponent> {

        val result = CompletableDeferred<ILangComponent>()

        scope.launch {
            result.complete(getMessage(messageKey, audience, *placeHolder))
        }

        return result
    }

    /*
    * Get a raw message from the datebase without parsing
    *
    * @param messageKey The key of the message in the database
    * @param langID The ID of the language
    *
    * @return The raw message as String
     */
    override fun getRawMessage(messageKey: String, languageID: Int): String {

        messageCache[languageID]?.get(messageKey)?.let {
            return it
        }

        val langMap = messageMap[languageID] ?: kotlin.run {
            messageMap[languageID] = ICoreAPI.INSTANCE.getRedisConnection().getRedissonClient()
                .getMap("lang:$languageID"); messageMap[languageID]!!
        }

        return langMap[messageKey] ?: kotlin.run {
            langMap[messageKey] = getRawMessage(messageKey, DEFAULT_LANGUAGE); langMap[messageKey]!!
        }
    }

    /*
    Get a raw message from the datebase without parsing
    This method is async and returns a Deferred<String>

    * @param messageKey The key of the message in the database
    * @param langID The ID of the language

    * @return The raw message as Deferred<String>
     */
    override fun getRawMessageAsync(messageKey: String, languageID: Int): Deferred<String> {

        val result = CompletableDeferred<String>()

        scope.launch {
            result.complete(getRawMessage(messageKey, languageID))
        }

        return result
    }

    /*
    * Get the language settings of a player
    *
    * @param audience The audience(usually a player) to get the settings from
    *
    * return The language settings as a String in the format "languageID;primaryColor;secondaryColor"
     */
    fun getPlayerSettings(audience: Audience): String {

        val uuid = audience.get(Identity.UUID).get().toString()

        playerCache[uuid]?.let {
            return it
        }

        return playerMap[uuid]
            ?: createPlayerSettings(audience)
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
        audience: Audience,
    ): String {

        val settings = "$DEFAULT_LANGUAGE;$DEFAULT_PRIMARY_COLOR;$DEFAULT_SECONDARY_COLOR"
        playerMap[audience.get(Identity.UUID).get().toString()] = settings

        return settings
    }
}