package net.dustrean.api.language

import net.dustrean.api.language.component.chat.ChatComponentBuilder
import net.dustrean.api.language.placeholder.collection.PlaceholderCollection
import java.util.*

interface ILanguagePlayer {

    val uuid: UUID
    var languageId: Int

    suspend fun sendMessage(provider: ChatComponentBuilder.LanguageChatComponentProvider)

    fun getPlaceholders(prefix: String = ""): PlaceholderCollection

}