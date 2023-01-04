package net.dustrean.api.language.component.item

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component

class ItemComponentProvider : LanguageComponentProvider(LanguageType.ITEM){
    var name = Component.empty()
    var lore = mutableListOf<Component>()
}