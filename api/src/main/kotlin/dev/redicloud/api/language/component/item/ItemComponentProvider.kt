package dev.redicloud.api.language.component.item

import dev.redicloud.api.language.LanguageType
import dev.redicloud.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component

class ItemComponentProvider : LanguageComponentProvider(LanguageType.ITEM){
    var name = Component.empty()
    var lore = mutableListOf<Component>()
}