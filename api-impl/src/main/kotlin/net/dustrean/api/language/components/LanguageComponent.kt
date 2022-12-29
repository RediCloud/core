package net.dustrean.api.language.components

import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.language.LanguageManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import java.util.UUID

/**
 * Interface for a language component
 *
 * @property messageKey The key of the message in the database
 * @property audience The audience(usually a player) to get the settings from
 * @property placeHolder The placeholders to replace in the message (<placeholder></placeholder>)
 *
 * More information about placeholders can be found here:
 * https://docs.adventure.kyori.net/minimessage/dynamic-replacements.html
 *
 * @usage:
 *  This can easily be sent to a player with the built-in sendMessage() method.
 *  Or you can use the Player.sendMSG|getMSG extensions method to get the message directly.
 */
class LanguageComponent(
    override val messageKey: String,
    override val uuid: UUID,
    vararg placeHolder: Pair<String, Component>
) : ILanguageComponent {

    private var component: Component

    companion object {
        val languageManager = ICoreAPI.getInstance<CoreAPI>().getLanguageManager()
    }
    init {

        val (langID, primaryColor, secondaryColor) = languageManager.getPlayerSettings(uuid)

        val message = try {
            languageManager.getRawMessage(messageKey, langID)
        } catch (e: Throwable) {
            null
        }

        component = if (message == null) {
            // message was not found
            Component.text("§cError:§7 {$messageKey} not found [placeholder: ${placeHolder.joinToString { it.first }}]")
        } else {
            // message was found and will be parsed
            MiniMessage.miniMessage().deserialize(
                message,
                Placeholder.component("pri", Component.text(primaryColor)),
                Placeholder.component("sec", Component.text(secondaryColor)),
                TagResolver.builder().apply { placeHolder.forEach { this.tag(it.first, Tag.inserting(it.second)) } }
                    .build(),
                TagResolver.resolver()
            )
        }

    }

    override fun asComponent() = component
}