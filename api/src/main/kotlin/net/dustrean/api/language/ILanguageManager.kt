package net.dustrean.api.language

import kotlinx.coroutines.Deferred
import net.dustrean.api.language.components.ILangComponent
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component

interface ILanguageManager {

    /*
    * Get a message from the datebase and parse placeholders and colors with miniMessage
    *
    * @param messageKey The key of the message in the database
    * @param audience The audience(usually a player) to get the settings from
    * @param placeHolder The placeholders to replace in the message (<placeholder></placeholder>)
    *
    * @return The parsed message as ILangComponent
     */
    fun getMessage(
        messageKey: String,
        audience: Audience,
        vararg placeHolder: Pair<String, Component>,
    ): ILangComponent

    /*
    * Get a message from the datebase and parse placeholders and colors with miniMessage
    * This method is async and returns a Deferred<ILangComponent>

    * @param messageKey The key of the message in the database
    * @param audience The audience(usually a player) to get the settings from
    * @param placeHolder The placeholders to replace in the message (<placeholder></placeholder>)
    *
    * @return The parsed message as Deferred<ILangComponent>
     */
    fun getMessageAsync(
        messageKey: String,
        audience: Audience,
        vararg placeHolder: Pair<String, Component>,
    ): Deferred<ILangComponent>

    /*
    * Get a raw message from the datebase without parsing
    *
    * @param messageKey The key of the message in the database
    * @param langID The ID of the language
    *
    * @return The raw message as String
     */
    fun getRawMessage(messageKey: String, languageID: Int): String

    /*
    Get a raw message from the datebase without parsing
    This method is async and returns a Deferred<String>

    * @param messageKey The key of the message in the database
    * @param langID The ID of the language

    * @return The raw message as Deferred<String>
     */
    fun getRawMessageAsync(messageKey: String, languageID: Int): Deferred<String>
}