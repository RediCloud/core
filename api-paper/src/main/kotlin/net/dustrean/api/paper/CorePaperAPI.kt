package net.dustrean.api.paper

import net.dustrean.api.cloud.CloudCoreAPI
import net.dustrean.api.paper.utils.parser.PlayerParser
import net.dustrean.api.utils.parser.string.StringParser

object CorePaperAPI : CloudCoreAPI() {

    init {
        StringParser.customTypeParsers.add(PlayerParser())
    }
}