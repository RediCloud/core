package net.dustrean.api.language.component.inventory

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.LanguageComponentProvider
import net.dustrean.api.language.component.item.ItemComponent
import net.dustrean.api.language.component.item.ItemComponentProvider
import net.kyori.adventure.text.Component

class InventoryComponentProvider : LanguageComponentProvider(LanguageType.INVENTORY) {
    var title: Component = Component.empty()
    var size: Int = -1
    var inventoryType = InventoryType.CHEST
    val itemProvider: MutableMap<Int, ItemComponentProvider> = mutableMapOf()
    val itemComponents: MutableMap<Int, ItemComponent> = mutableMapOf()
}