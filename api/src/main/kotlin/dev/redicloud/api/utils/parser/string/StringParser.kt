package dev.redicloud.api.utils.parser.string

import dev.redicloud.api.utils.parser.ITypeFromClassParser
import dev.redicloud.api.utils.parser.string.typeparser.*


object StringParser : ITypeFromClassParser<String> {

    private val parsableTypes = listOf(String::class.java)
    val customTypeParsers = mutableListOf<IStringTypeParser<out Any>>(
        UUIDParser(),
        BooleanParser(),
        IntParser(),
        DoubleParser(),
        FloatParser(),
        LongParser(),
    )

    override fun supportedTypes(): Set<Class<out Any>> =
        customTypeParsers.map { it.allowedTypes() }.flatten().union(parsableTypes)

    override fun <R : Any> parseToObject(value: String, clazz: Class<R>): R? {

        val parser = customTypeParsers.firstOrNull { it.allowedTypes().contains(clazz) }
        parser ?: throw IllegalArgumentException("Can't parse class to ${clazz.simpleName}: No parser found.")
        parser as IStringTypeParser<out R>
        return parser.parse(value)
    }

}