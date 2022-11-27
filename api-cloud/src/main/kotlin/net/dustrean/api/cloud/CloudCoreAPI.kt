package net.dustrean.api.cloud

import net.dustrean.api.CoreAPI
import net.dustrean.api.cloud.utils.getCurrentNetworkComponent

abstract class CloudCoreAPI() : CoreAPI(getCurrentNetworkComponent()){

}