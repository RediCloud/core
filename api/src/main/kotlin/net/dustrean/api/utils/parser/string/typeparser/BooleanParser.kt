package net.dustrean.api.utils.parser.string.typeparser

import net.dustrean.api.utils.parser.string.IStringTypeParser

class BooleanParser : IStringTypeParser<Boolean> {

    override fun allowedTypes(): List<Class<out Boolean>> = listOf(Boolean::class.java)

    override fun parse(value: String): Boolean? {
        when (value.lowercase()) {
            "yes" -> return true
            "no" -> return false
            "true" -> return true
            "false" -> return false
        }
        return null
    }
}