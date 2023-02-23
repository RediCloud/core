package dev.redicloud.api.paper

import dev.redicloud.api.command.ICommandManager
import dev.redicloud.api.paper.command.PaperCommandManager
import dev.redicloud.api.paper.event.PlayerEvents
import dev.redicloud.api.paper.language.PaperLanguageBridge
import dev.redicloud.api.paper.utils.parser.PlayerParser
import dev.redicloud.api.utils.ExceptionHandler
import dev.redicloud.api.utils.parser.string.StringParser
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

object CorePaperAPI : dev.redicloud.api.cloud.CloudCoreAPI() {


    override val languageBridge = PaperLanguageBridge()
    override val commandManager: ICommandManager = PaperCommandManager
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