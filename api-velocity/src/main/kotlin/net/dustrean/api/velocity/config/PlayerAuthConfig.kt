package net.dustrean.api.velocity.config

import net.dustrean.api.config.IConfig

class PlayerAuthConfig(override val key: String) : IConfig {

    val crackAllowed = false
    val minPasswordLength = 6
    val maxPasswordLength = 18
    val passwordCanContainsPlayerName = false
    val verifyTask = "Verify"
    val passwordRounds = 10

}