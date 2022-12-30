package net.dustrean.api.cloud.language

import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.cloud.utils.getCloudPlayerManager
import net.dustrean.api.language.ILanguageBridge
import net.dustrean.api.language.ILanguagePlayer
import net.dustrean.api.language.component.chat.ChatComponent
import net.dustrean.api.language.component.chat.ChatComponentBuilder
import net.dustrean.api.language.component.tablist.TabListComponent
import net.dustrean.api.language.component.tablist.TabListComponentBuilder

class LanguageBridge : ILanguageBridge {


    override suspend fun sendMessage(
        player: ILanguagePlayer,
        provider: ChatComponentBuilder.LanguageChatComponentProvider,
        chatComponent: ChatComponent
    ) {
        val textComponent = ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
            chatComponent.rawComponent,
            chatComponent.serializerType,
            provider.placeholderProvider.parse(chatComponent.rawComponent)
        )
        getCloudPlayerManager().playerExecutor(player.uuid).sendChatMessage(textComponent)
    }

    override suspend fun sendTabList(
        player: ILanguagePlayer,
        provider: TabListComponentBuilder.LanguageTabListComponentProvider,
        tabListComponent: TabListComponent
    ) {
        val header = ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
            tabListComponent.rawHeader,
            tabListComponent.serializerType,
            provider.placeholderProvider.parse(tabListComponent.rawHeader)
        )
        val footer = ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
            tabListComponent.rawFooter,
            tabListComponent.serializerType,
            provider.placeholderProvider.parse(tabListComponent.rawFooter)
        )
        getCloudPlayerManager().playerExecutor(player.uuid).sendTabList(header, footer)
    }
}