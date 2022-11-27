package net.dustrean.api.paper.boostrap

import net.dustrean.api.paper.CorePaperAPI
import org.bukkit.plugin.java.JavaPlugin

class PaperBootstrap : JavaPlugin(){

    //Set by reflection on plugin load
    val coreAPI: CorePaperAPI = null!!

    override fun onEnable() {
        this::class.java.getDeclaredField("coreAPI").apply {
            isAccessible = true
            set(this@PaperBootstrap, CorePaperAPI())
            isAccessible = false
        }
    }

}