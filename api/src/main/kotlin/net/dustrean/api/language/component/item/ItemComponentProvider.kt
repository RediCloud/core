package net.dustrean.api.language.component.item

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.LanguageComponentProvider
import net.kyori.adventure.text.Component
import java.util.UUID

class ItemComponentProvider : LanguageComponentProvider(LanguageType.ITEM){
    lateinit var material: Enum<*>
    var name = Component.empty()
    var amount = 1
    var damage = 0
    var lore = mutableListOf<Component>()
    var unbreakable = false
    var blockDrop = false
    var blockInteract = false
    var blockClick = false
    var permission: String? = null
    var skullOwner: UUID? = null
    var skullTexture: String? = null
    var properties = mutableMapOf<String, String>()
}