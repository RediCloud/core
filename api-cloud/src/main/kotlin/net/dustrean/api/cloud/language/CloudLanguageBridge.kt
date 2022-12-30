package net.dustrean.api.cloud.language

import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.cloud.utils.getCloudPlayerManager
import net.dustrean.api.language.ILanguageBridge
import net.dustrean.api.language.ILanguagePlayer
import net.dustrean.api.language.component.chat.ChatComponent
import net.dustrean.api.language.component.chat.ChatComponentProvider
import net.dustrean.api.language.component.tablist.TabListComponent
import net.dustrean.api.language.component.tablist.TabListComponentProvider
import net.dustrean.api.language.component.title.TitleComponent
import net.dustrean.api.language.component.title.TitleComponentProvider
import net.dustrean.api.language.placeholder.PlaceholderProvider
import net.kyori.adventure.title.Title
import java.time.Duration

abstract class CloudLanguageBridge : ILanguageBridge {


    override suspend fun sendMessage(
        player: ILanguagePlayer,
        provider: ChatComponentProvider,
        chatComponent: ChatComponent
    ) {
        val placeholderProvider = PlaceholderProvider().apply(provider.placeholderProvider)
        val textComponent = ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
            chatComponent.rawComponent,
            chatComponent.serializerType,
            placeholderProvider.parse(chatComponent.rawComponent)
        )
        getCloudPlayerManager().playerExecutor(player.uuid).sendChatMessage(textComponent)
    }

    override suspend fun sendTitle(
        player: ILanguagePlayer,
        provider: TitleComponentProvider,
        titleComponent: TitleComponent
    ) {
        val placeholderProvider = PlaceholderProvider().apply(provider.placeholderProvider)
        val title = Title.title(
            ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
                titleComponent.rawTitleComponent,
                titleComponent.serializerType,
                placeholderProvider.parse(titleComponent.rawTitleComponent)
            ),
            ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
                titleComponent.rawSubtitleComponent,
                titleComponent.serializerType,
                placeholderProvider.parse(titleComponent.rawSubtitleComponent)
            ),
            Title.Times.times(
                Duration.ofMillis(titleComponent.fadeIn),
                Duration.ofMillis(titleComponent.stay),
                Duration.ofMillis(titleComponent.fadeOut)
            )
        )
        getCloudPlayerManager().playerExecutor(player.uuid).sendTitle(title)
    }

}