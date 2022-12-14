package net.dustrean.api.paper

import net.dustrean.api.cloud.CloudCoreAPI
import net.dustrean.api.paper.utils.parser.PlayerParser
import net.dustrean.api.utils.parser.string.StringParser
import org.bukkit.plugin.java.JavaPlugin

object CorePaperAPI : CloudCoreAPI() {

    //set by reflection on plugin load
    val plugin: JavaPlugin = null!!

    init {
        StringParser.customTypeParsers.add(PlayerParser())
    }

    fun init(plugin: JavaPlugin){
        this::class.java.getDeclaredField("plugin").apply {
            isAccessible = true
            set(this@CorePaperAPI, plugin)
            isAccessible = false
        }
    }
}