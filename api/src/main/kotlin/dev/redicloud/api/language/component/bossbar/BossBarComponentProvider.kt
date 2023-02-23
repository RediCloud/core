package dev.redicloud.api.language.component.bossbar

import dev.redicloud.api.language.LanguageType
import dev.redicloud.api.language.component.LanguageComponentProvider
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component

class BossBarComponentProvider : LanguageComponentProvider(LanguageType.BOSS_BAR) {
    var name: Component = Component.empty()
}