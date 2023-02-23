package dev.redicloud.api.utils.parser.string.typeparser

import dev.redicloud.api.utils.parser.string.IStringTypeParser
import java.util.*

class UUIDParser : IStringTypeParser<UUID> {
    override fun parse(value: String): UUID {
        return UUID.fromString(value)
    }

    override fun allowedTypes(): List<Class<out UUID>> {
        return listOf(UUID::class.java)
    }
}