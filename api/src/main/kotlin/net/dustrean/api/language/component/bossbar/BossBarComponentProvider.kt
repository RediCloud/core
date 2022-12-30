package net.dustrean.api.language.component.bossbar

import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.LanguageComponentProvider
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component

class BossBarComponentProvider : LanguageComponentProvider(LanguageType.BOSS_BAR) {
    var name: Component = Component.empty()
    var progress: Float = 1.0f
    var color: BossBar.Color = BossBar.Color.RED
    var overlay: BossBar.Overlay = BossBar.Overlay.PROGRESS
}