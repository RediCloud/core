package net.dustrean.api.language.component.scoreboard

import net.dustrean.api.language.LanguageSerializerType
import net.dustrean.api.language.LanguageType
import net.dustrean.api.language.component.ILanguageComponent

class ScoreboardLineComponent(
    override val key: String,
    override val languageId: Int,
    override val type: LanguageType,
    override val serializerType: LanguageSerializerType,
    val rawContent: String,
    val id: String,
    val score: Int,
    val lineType: ScoreboardLineType
) : ILanguageComponent

enum class ScoreboardLineType {
    TITLE,
    LINE;
}