package dev.redicloud.api.cloud

import dev.redicloud.api.CoreAPI
import dev.redicloud.api.cloud.utils.currentCloudServiceInfo
import dev.redicloud.api.cloud.utils.getCurrentNetworkComponent
import dev.redicloud.api.utils.ExceptionHandler

abstract class CloudCoreAPI : CoreAPI(getCurrentNetworkComponent()) {

    init {
        ExceptionHandler.service = currentCloudServiceInfo.name()
    }

}