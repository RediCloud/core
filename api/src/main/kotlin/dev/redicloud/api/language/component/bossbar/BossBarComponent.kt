package dev.redicloud.api.language.component.bossbar

import dev.redicloud.api.language.LanguageSerializerType
import dev.redicloud.api.language.LanguageType
import dev.redicloud.api.language.component.ILanguageComponent
import net.kyori.adventure.bossbar.BossBar

class BossBarComponent(
    override val key: String,
    override val languageId: Int,
    override val type: LanguageType,
    override val serializerType: LanguageSerializerType,
    val rawName: String
) : ILanguageComponent