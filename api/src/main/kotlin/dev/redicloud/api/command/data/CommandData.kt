package dev.redicloud.api.command.data

import dev.redicloud.api.command.ICommand
import java.lang.reflect.Method

class CommandData(

    val path: String,
    val method: Method,
    val source: ICommand,
    val permission: String,
    val parameterDataList: ArrayList<CommandParameterData> = ArrayList(),
    private val aliases: Array<String> = arrayOf()
) {

    fun getIndexOfParameter(parameterName: String) = path.split(" ").indexOf("<$parameterName>")

    fun getParameterDataByName(name: String) = this.parameterDataList.firstOrNull { it.name == name }

    fun getParameterDataByIndex(index: Int) = getParameterDataByName(this.path.split(" ")[index - 1])

    fun getParameterDataByNameWithBraces(name: String) = getParameterDataByName(name.drop(1).dropLast(1))

    fun getAllPathsWithAliases(): Collection<String> {
        val path = path.split(" ").drop(1).joinToString(" ")
        return aliases.map { path }.union(listOf(path))
    }
}