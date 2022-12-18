package net.dustrean.api.paper

import net.dustrean.api.cloud.CloudCoreAPI
import net.dustrean.api.paper.command.PaperCommandManager
import net.dustrean.api.paper.utils.parser.PlayerParser
import net.dustrean.api.utils.ExceptionHandler
import net.dustrean.api.utils.parser.string.StringParser

object CorePaperAPI : CloudCoreAPI() {

    override fun getCommandManager() = PaperCommandManager

    init {

        ExceptionHandler

        StringParser.customTypeParsers.add(PlayerParser())

    }

    fun init() {

    }
}