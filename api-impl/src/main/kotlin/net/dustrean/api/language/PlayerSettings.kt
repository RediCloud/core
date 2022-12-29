package net.dustrean.api.language

data class PlayerSettings(
    val languageID: Int = LanguageManager.DEFAULT_LANGUAGE,
    val primaryColor: String = LanguageManager.DEFAULT_PRIMARY_COLOR,
    val secondaryColor: String = LanguageManager.DEFAULT_SECONDARY_COLOR,
)
