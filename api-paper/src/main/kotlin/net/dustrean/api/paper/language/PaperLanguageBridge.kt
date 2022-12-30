package net.dustrean.api.paper.language

import net.dustrean.api.CoreAPI
import net.dustrean.api.ICoreAPI
import net.dustrean.api.cloud.language.CloudLanguageBridge
import net.dustrean.api.language.ILanguagePlayer
import net.dustrean.api.language.component.tablist.TabListComponent
import net.dustrean.api.language.component.tablist.TabListComponentProvider
import net.dustrean.api.language.placeholder.PlaceholderProvider
import org.bukkit.Bukkit

class PaperLanguageBridge : CloudLanguageBridge() {

    override suspend fun sendTabList(
        player: ILanguagePlayer,
        provider: TabListComponentProvider,
        tabListComponent: TabListComponent
    ) {
        val placeholderProvider = PlaceholderProvider().apply(provider.placeholderProvider)
        val header = ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
            tabListComponent.rawHeaderComponent,
            tabListComponent.serializerType,
            placeholderProvider.parse(tabListComponent.rawHeaderComponent)
        )
        val footer = ICoreAPI.getInstance<CoreAPI>().getLanguageManager().deserialize(
            tabListComponent.rawFooterComponent,
            tabListComponent.serializerType,
            placeholderProvider.parse(tabListComponent.rawFooterComponent)
        )
        val paperPlayer = Bukkit.getPlayer(player.uuid) ?: return
        paperPlayer.sendPlayerListHeaderAndFooter(header, footer)
    }

}