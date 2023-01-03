package net.dustrean.api.language.component.bossbar

import net.dustrean.api.language.LanguageSerializerType
import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.ILanguageComponent
import net.kyori.adventure.bossbar.BossBar

class BossBarComponent(
    override val key: String,
    override val languageId: Int,
    override val type: LanguageType,
    override val serializerType: LanguageSerializerType,
    val rawName: String,
    val progress: Float,
    val color: BossBar.Color,
    val overlay: BossBar.Overlay
) : ILanguageComponent