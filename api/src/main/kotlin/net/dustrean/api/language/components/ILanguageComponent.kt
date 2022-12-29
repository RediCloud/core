package net.dustrean.api.language.components

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.ComponentLike
import java.util.UUID

/**
 * Interface for a language component
 *
 * @property messageKey The key of the message in the database
 * @property audience The audience(usually a player) to get the settings from
 *
 * @see net.dustrean.api.language.components.LanguageComponent
 *
 * @usage:
 *  This can easily be sent to a player with the built-in sendMessage() method.
 *  Or you can use the Player.sendMSG|getMSG extensions method to get the message directly.
 */
interface ILanguageComponent : ComponentLike {

    val messageKey: String

    val uuid: UUID?

}