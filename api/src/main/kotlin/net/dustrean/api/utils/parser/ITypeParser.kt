package net.dustrean.api.utils.parser

/**
 * Parses a object from one type to another
 */
interface ITypeParser<T : Any, R : Any> {

    /**
     * Parses the object [T] to [R]
     * @return null if the parse failed
     */
    fun parse(value: T): R?

    /**
     * Returns a list of types this parser can parse
     */
    fun allowedTypes(): List<Class<out R>>

}