package net.dustrean.api.language.component.item

import net.dustrean.api.language.LanguageSerializerType
import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.ILanguageComponent
import java.util.*

class ItemComponent(
    override val key: String,
    override val languageId: Int,
    override val type: LanguageType,
    override val serializerType: LanguageSerializerType,
    val rawMaterial: String,
    val rawName: String,
    val amount: Int,
    val damage: Int,
    val rawLore: List<String>,
    val unbreakable: Boolean,
    val permission: String?,
    val skullOwner: UUID?,
    val skullTexture: String?,
    val properties: Map<String, String>
) : ILanguageComponent