package com.dustrean.api.packet.futures

interface FutureMapper<T, R> {
    fun map(t: T): R
}