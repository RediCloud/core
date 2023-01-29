package net.dustrean.api.cloud

import net.dustrean.api.CoreAPI
import net.dustrean.api.cloud.language.CloudLanguageBridge
import net.dustrean.api.cloud.utils.getCurrentNetworkComponent
import net.dustrean.api.cloud.utils.getCurrentServerInfo
import net.dustrean.api.language.ILanguageBridge
import net.dustrean.api.utils.ExceptionHandler

abstract class CloudCoreAPI : CoreAPI(getCurrentNetworkComponent()) {

    init {
        ExceptionHandler.service = getCurrentServerInfo().name()
    }

}