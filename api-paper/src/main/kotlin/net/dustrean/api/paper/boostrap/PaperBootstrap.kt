package net.dustrean.api.paper.boostrap

import net.dustrean.api.paper.CorePaperAPI
import org.bukkit.plugin.java.JavaPlugin

class PaperBootstrap : JavaPlugin(){

    override fun onEnable() {
        CorePaperAPI.init(this)
    }

    override fun onDisable() {
        CorePaperAPI.shutdown()
    }

}