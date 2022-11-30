package net.dustrean.api.utils.parser.string.typeparser

import net.dustrean.api.utils.parser.string.IStringTypeParser

class FloatParser : IStringTypeParser<Float> {
    override fun parse(value: String): Float? {
        return value.toFloatOrNull()
    }

    override fun allowedTypes(): List<Class<out Float>> {
        return listOf(Float::class.java)
    }
}