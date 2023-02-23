package dev.redicloud.api.utils.parser.string.typeparser

import dev.redicloud.api.utils.parser.string.IStringTypeParser

class LongParser : IStringTypeParser<Long> {
    override fun parse(value: String): Long? {
        return value.toLongOrNull()
    }

    override fun allowedTypes(): List<Class<out Long>> {
        return listOf(Long::class.java)
    }
}