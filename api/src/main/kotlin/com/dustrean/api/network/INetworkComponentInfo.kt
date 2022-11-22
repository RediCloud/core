package com.dustrean.api.network

import java.util.UUID

interface INetworkComponentInfo {

    var key: String
    var uniqueId: UUID
    var type: NetworkComponentType

}