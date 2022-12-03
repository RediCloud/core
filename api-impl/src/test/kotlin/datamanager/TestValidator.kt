package datamanager

import datamanager.TestPlayer
import net.dustrean.api.ICoreAPI
import net.dustrean.api.data.ICacheValidator
import net.dustrean.api.network.NetworkComponentInfo
import net.dustrean.api.network.NetworkComponentType
import java.util.*

class TestValidator : ICacheValidator<TestPlayer> {

    override fun isValid(): Boolean = ICoreAPI.INSTANCE.getNetworkComponentInfo() == NetworkComponentInfo(
        NetworkComponentType.STANDALONE, UUID.fromString("b04e8b0d-d274-4069-b7a6-fb26d7f17357")
    )

}