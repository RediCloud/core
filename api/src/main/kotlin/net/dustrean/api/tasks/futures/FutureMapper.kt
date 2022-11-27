package net.dustrean.api.tasks.futures

interface FutureMapper<T, R> {
    fun map(t: T): R
}