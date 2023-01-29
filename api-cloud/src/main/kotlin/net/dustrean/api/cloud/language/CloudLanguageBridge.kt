package net.dustrean.api.cloud.language

import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.cloud.utils.getCloudPlayerManager
import net.dustrean.api.language.ILanguageBridge
import net.dustrean.api.language.ILanguagePlayer
import net.dustrean.api.language.component.chat.ChatComponent
import net.dustrean.api.language.component.chat.ChatComponentProvider
import net.dustrean.api.language.component.title.TitleComponent
import net.dustrean.api.language.component.title.TitleComponentProvider
import net.dustrean.api.language.placeholder.PlaceholderProvider
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import java.time.Duration

abstract class CloudLanguageBridge : ILanguageBridge {


    override suspend fun sendMessage(
        player: ILanguagePlayer,
        provider: ChatComponentProvider,
        chatComponent: ChatComponent
    ): Component? {
        val placeholderProvider = PlaceholderProvider().apply(provider.placeholderProvider)
        val textComponent = ICoreAPI.getInstance<CoreAPI>().languageManager.deserialize(
            chatComponent.rawMessage,
            chatComponent.serializerType,
            placeholderProvider.parse(chatComponent.rawMessage)
        )
        getCloudPlayerManager().playerExecutor(player.uuid).sendChatMessage(textComponent)
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
            ),
            ICoreAPI.getInstance<CoreAPI>().languageManager.deserialize(
                titleComponent.rawSubtitle,
                titleComponent.serializerType,
                placeholderProvider.parse(titleComponent.rawSubtitle)
            ),
            Title.Times.times(
                fadeIn,
                stay,
                fadeOut
            )
        )
        getCloudPlayerManager().playerExecutor(player.uuid).sendTitle(title)
        return title
    }

}