package net.dustrean.api.velocity.command

import com.mojang.brigadier.tree.CommandNode
import com.velocitypowered.api.command.CommandMeta
import com.velocitypowered.api.command.CommandSource
import net.dustrean.api.velocity.VelocityCoreAPI

class DefaultCommandMeta(
    private val aliase: Array<String>,
    private val hint: MutableCollection<CommandNode<CommandSource>> = mutableListOf()
) : CommandMeta {

    /**
     * Returns a non-empty collection containing the case-insensitive aliases
     * used to execute the command.
     *
     * @return the command aliases
     */
    override fun getAliases(): MutableCollection<String> {
        return aliase.toMutableList()
    }

    /**
     * Returns an immutable collection containing command nodes that provide
     * additional argument metadata and tab-complete suggestions.
     * Note some [Command] implementations may not support hinting.
     *
     * @return the hinting command nodes
     */
    override fun getHints(): MutableCollection<CommandNode<CommandSource>> {
        return hint
    }

    override fun getPlugin(): Any {
        return VelocityCoreAPI
    }

}