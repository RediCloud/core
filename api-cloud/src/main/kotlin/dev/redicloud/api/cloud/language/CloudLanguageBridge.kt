package dev.redicloud.api.cloud.language

import dev.redicloud.api.CoreAPI
import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.cloud.utils.cloudPlayerManager
import dev.redicloud.api.language.ILanguageBridge
import dev.redicloud.api.language.ILanguagePlayer
import dev.redicloud.api.language.component.chat.ChatComponent
import dev.redicloud.api.language.component.chat.ChatComponentProvider
import dev.redicloud.api.language.component.title.TitleComponent
import dev.redicloud.api.language.component.title.TitleComponentProvider
import dev.redicloud.api.language.placeholder.PlaceholderProvider
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import java.time.Duration

abstract class CloudLanguageBridge : ILanguageBridge {


    override suspend fun sendMessage(
        player: ILanguagePlayer, provider: ChatComponentProvider, chatComponent: ChatComponent
    ): Component? {
        val placeholderProvider = PlaceholderProvider().apply(provider.placeholderProvider)
        val textComponent = ICoreAPI.getInstance<CoreAPI>().languageManager.deserialize(
            chatComponent.rawMessage, chatComponent.serializerType, placeholderProvider.parse(chatComponent.rawMessage)
        )
        cloudPlayerManager.playerExecutor(player.uuid).sendChatMessage(textComponent)
        return textComponent
    }

    override suspend fun sendTitle(
        player: ILanguagePlayer,
        provider: TitleComponentProvider,
        titleComponent: TitleComponent,
        fadeIn: Duration,
        stay: Duration,
        fadeOut: Duration
    ): Title? {
        val placeholderProvider = PlaceholderProvider().apply(provider.placeholderProvider)
        val title = Title.title(
            ICoreAPI.getInstance<CoreAPI>().languageManager.deserialize(
                titleComponent.rawTitle,
                titleComponent.serializerType,
                placeholderProvider.parse(titleComponent.rawTitle)
            ), ICoreAPI.getInstance<CoreAPI>().languageManager.deserialize(
                titleComponent.rawSubtitle,
                titleComponent.serializerType,
                placeholderProvider.parse(titleComponent.rawSubtitle)
            ), Title.Times.times(
                fadeIn, stay, fadeOut
            )
        )
        cloudPlayerManager.playerExecutor(player.uuid).sendTitle(title)
        return title
    }

}