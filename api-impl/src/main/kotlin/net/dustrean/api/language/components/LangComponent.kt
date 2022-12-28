package net.dustrean.api.language.components

import net.dustrean.api.language.LanguageManger
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

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
class LangComponent(
    override val messageKey: String,
    override val audience: Audience,
    vararg placeHolder: Pair<String, Component>
) : ILangComponent {

    private var component: Component

    init {

        val (langID, primaryColor, secondaryColor) = LanguageManger.getPlayerSettings(audience).split(";")

        val message = LanguageManger.getRawMessage(messageKey, langID.toInt())

        if (message == "") {
            // message was not found
            component =
                Component.text("§cError:§7 {$messageKey} not found [placeholder: ${placeHolder.joinToString { it.first }}]")
        } else {

            // message was found and will be parsed
            MiniMessage.miniMessage().deserialize(
                message,
                Placeholder.component("pColor", Component.text(primaryColor)),
                Placeholder.component("sColor", Component.text(secondaryColor)),
                TagResolver.builder().apply { placeHolder.forEach { this.tag(it.first, Tag.inserting(it.second)) } }
                    .build()
            ).let {
                component = it
            }
        }

    }

    override fun asComponent() = component
}