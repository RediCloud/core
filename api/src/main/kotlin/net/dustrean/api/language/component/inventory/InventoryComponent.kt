package net.dustrean.api.language.component.inventory

import net.dustrean.api.language.LanguageSerializerType
import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.ILanguageComponent

class InventoryComponent(
    override val key: String,
    override val languageId: Int,
    override val type: LanguageType,
    override val serializerType: LanguageSerializerType,
    val rawTitle: String,
    val rawItems: Map<Int, String>
) : ILanguageComponent

enum class InventoryType(val maxSize: Int){
    CHEST(54),
    DISPENSER(9),
    DROPPER(9),
    FURNACE(3),
    WORKBENCH(10),
    ENCHANTING(2),
    BREWING(5),
    ENDER_CHEST(27),
    ANVIL(3),
    SMITHING(3),
    BEACON(1),
    HOPPER(5),
    SHULKER_BOX(27),
    BARREL(27),
    BLAST_FURNACE(3),
    LECTERN(1),
    SMOKER(3),
    LOOM(4),
    CARTOGRAPHY(3),
    GRINDSTONE(3),
    STONECUTTER(2),
    COMPOSTER(1)
}