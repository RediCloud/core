package net.dustrean.api.utils.extension

import com.google.gson.Gson
import net.dustrean.api.ICoreAPI
import org.redisson.api.RList
import org.redisson.api.RedissonClient


val gson = Gson()

data class JsonObjectData(val json: String, val clazz: String)

fun <T> JsonObjectData.toObject(): T = gson.fromJson(json, Class.forName(clazz)) as T

fun Any.toJsonObjectData(): JsonObjectData = JsonObjectData(gson.toJson(this), this::class.java.name)

fun <R> RedissonClient.getExternalList(name: String, src: R): ExternalRList<R> =
    ExternalRList(ICoreAPI.INSTANCE.getRedisConnection().getRedissonClient().getList(name))

class ExternalRList<V>(private val sourceList: RList<JsonObjectData>) {

    fun readAll(): List<V> = sourceList.readAll().map { it.toObject() }

    fun add(element: V) = sourceList.add(element!!.toJsonObjectData())

    fun remove(element: V) = sourceList.remove(element!!.toJsonObjectData())

    fun contains(element: V) = sourceList.contains(element!!.toJsonObjectData())

    fun size() = sourceList.size

    fun isEmpty() = sourceList.isEmpty()

    fun addAll(elements: Collection<V>) = sourceList.addAll(elements.map { it!!.toJsonObjectData() })

    fun removeAll(elements: Collection<V>) = sourceList.removeAll(elements.map { it!!.toJsonObjectData() })

    fun clear() = sourceList.clear()

    fun get(index: Int): V = sourceList.get(index).toObject()

    fun removeIf(filter: (V) -> Boolean) = sourceList.removeIf { filter(it.toObject()) }

}