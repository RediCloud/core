package datamanager

import net.dustrean.api.data.AbstractDataManager
import net.dustrean.api.redis.IRedisConnection

class TestPlayerManager(connection: IRedisConnection) : AbstractDataManager<TestPlayer>(
    "testplayer", connection, TestPlayer::class.java
){



}