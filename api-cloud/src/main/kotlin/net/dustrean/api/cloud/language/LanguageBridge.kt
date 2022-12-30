package net.dustrean.api.cloud.language

import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.cloud.utils.getCloudPlayerManager
import net.dustrean.api.language.ILanguageBridge
import net.dustrean.api.language.ILanguagePlayer
import net.dustrean.api.language.component.chat.ChatComponent
import net.dustrean.api.language.component.chat.ChatComponentBuilder

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
}