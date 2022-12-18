package net.dustrean.api.cloud

import net.dustrean.api.CoreAPI
import net.dustrean.api.cloud.utils.getCurrentNetworkComponent
import net.dustrean.api.utils.ExceptionHandler

abstract class CloudCoreAPI() : CoreAPI(getCurrentNetworkComponent()) {

    init {

        ExceptionHandler
        ExceptionHandler::class.objectInstance?.javaClass?.getDeclaredField("service")?.apply {
            isAccessible = true
            set(
                ExceptionHandler::class.objectInstance, "CloudCoreAPI"
            )
            isAccessible = false
        }
    }
}