package net.dustrean.api.minestom

import net.dustrean.api.cloud.CloudCoreAPI
import net.dustrean.api.minestom.command.MinestomCommandManager
import net.dustrean.api.minestom.utils.parser.PlayerParser
import net.dustrean.api.utils.parser.string.StringParser

object MinestomCoreAPI : CloudCoreAPI() {

    override fun getCommandManager() = MinestomCommandManager

    fun init() {

        StringParser.customTypeParsers.add(PlayerParser())
    }

}