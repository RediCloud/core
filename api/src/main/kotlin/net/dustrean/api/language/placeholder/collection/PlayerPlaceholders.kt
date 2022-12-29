package net.dustrean.api.language.placeholder.collection

import net.dustrean.api.ICoreAPI
import net.dustrean.api.language.placeholder.Placeholder
import net.dustrean.api.player.IPlayer
import java.util.*

class PlayerPlaceholders(prefix: String) : PlaceholderCollection(prefix) {

    init {
        add(Placeholder("username") { getPlayer(it).name })
        add(Placeholder("uuid") { getPlayer(it).uuid.toString() })
    }

    private suspend fun getPlayer(uniqueId: UUID): IPlayer {
        return ICoreAPI.INSTANCE.getPlayerManager().getPlayerByUUID(uniqueId)!!
    }

}