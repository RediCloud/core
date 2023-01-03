package net.dustrean.api.event.impl

import net.dustrean.api.event.EventType
import net.dustrean.api.language.placeholder.PlaceholderProvider
import net.dustrean.api.player.IPlayer

class PlayerWithTargetEvent(type: EventType, player: IPlayer, val target: IPlayer) : PlayerEvent(type, player) {
    override fun reference(provider: PlaceholderProvider) {
        super.reference(provider)
        provider.placeholderCollections+=target.getPlaceholders("target")
    }
}