package net.dustrean.api.cloud

import net.dustrean.api.CoreAPI
import net.dustrean.api.cloud.language.LanguageBridge
import net.dustrean.api.cloud.utils.getCurrentNetworkComponent
import net.dustrean.api.utils.ExceptionHandler

abstract class CloudCoreAPI : CoreAPI(getCurrentNetworkComponent()) {

    init {
        ExceptionHandler.service = "CloudCoreAPI"
    }

    private val languageBridge = LanguageBridge()

    override fun getLanguageBridge() = languageBridge
    
}