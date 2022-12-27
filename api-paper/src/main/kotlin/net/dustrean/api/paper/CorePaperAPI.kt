package net.dustrean.api.paper

import net.dustrean.api.cloud.CloudCoreAPI
import net.dustrean.api.paper.command.PaperCommandManager
import net.dustrean.api.paper.event.PlayerEvents
import net.dustrean.api.paper.utils.parser.PlayerParser
import net.dustrean.api.utils.ExceptionHandler
import net.dustrean.api.utils.parser.string.StringParser
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

object CorePaperAPI : CloudCoreAPI() {

    override fun getCommandManager() = PaperCommandManager

    lateinit var plugin: JavaPlugin
        private set

    init {
        ExceptionHandler
    }

    fun init(plugin: JavaPlugin) {
        this.plugin = plugin
        StringParser.customTypeParsers.add(PlayerParser())
        Bukkit.getPluginManager().registerEvents(PlayerEvents(), plugin)
    }
}