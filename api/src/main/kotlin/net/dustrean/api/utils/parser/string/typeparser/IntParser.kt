package net.dustrean.api.utils.parser.string.typeparser

import net.dustrean.api.utils.parser.string.IStringTypeParser

class IntParser : IStringTypeParser<Int> {
    override fun parse(value: String): Int? {
        return value.toIntOrNull()
    }

    override fun allowedTypes(): List<Class<out Int>> {
        return listOf(Int::class.java)
    }
}