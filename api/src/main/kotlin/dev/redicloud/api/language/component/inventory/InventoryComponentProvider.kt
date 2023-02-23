package dev.redicloud.api.language.component.inventory

import dev.redicloud.api.language.LanguageType
import dev.redicloud.api.language.component.LanguageComponentProvider
import dev.redicloud.api.language.component.item.ItemComponent
import dev.redicloud.api.language.component.item.ItemComponentProvider
import net.kyori.adventure.text.Component

class InventoryComponentProvider : LanguageComponentProvider(LanguageType.INVENTORY) {
    var title: Component = Component.empty()
}