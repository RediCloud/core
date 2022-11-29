package net.dustrean.api.utils.parser.string.typeparser

import net.dustrean.api.utils.parser.string.IStringTypeParser

class DoubleParser : IStringTypeParser<Double> {
    override fun parse(value: String): Double? {
        return value.toDoubleOrNull()
    }

    override fun allowedTypes(): List<Class<out Double>> {
        return listOf(Double::class.java)
    }
}