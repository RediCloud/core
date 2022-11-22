package com.dustrean.api.packet.coroutines

interface FutureMapper<T, R> {
    fun map(t: T): R
}