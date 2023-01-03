package net.dustrean.api.cloud

import net.dustrean.api.CoreAPI
import net.dustrean.api.cloud.language.CloudLanguageBridge
import net.dustrean.api.cloud.utils.getCurrentNetworkComponent
import net.dustrean.api.utils.ExceptionHandler

abstract class CloudCoreAPI : CoreAPI(getCurrentNetworkComponent()) {

    init {
        ExceptionHandler.service = "CloudCoreAPI"
    }
    
}