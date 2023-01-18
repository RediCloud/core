package net.dustrean.api.utils.extension

import com.google.gson.Gson
import net.dustrean.api.ICoreAPI
import org.redisson.api.RList
import org.redisson.api.RedissonClient


val gson = Gson()

data class JsonObjectData(val json: String, val clazz: String)

fun <T> JsonObjectData.toObject(): T = gson.fromJson(json, Class.forName(clazz)) as T

fun Any.toJsonObjectData(): JsonObjectData = JsonObjectData(gson.toJson(this), this::class.java.name)

fun <R> RedissonClient.getExternalList(name: String): ExternalRList<R> =
    ExternalRList(ICoreAPI.INSTANCE.getRedisConnection().getRedissonClient().getList(name))

class ExternalRList<V>(private val sourceList: RList<JsonObjectData>): List<V> {

    fun readAll(): List<V> = sourceList.readAll().map { it.toObject() }

    fun add(element: V) = sourceList.add(element!!.toJsonObjectData())

    fun remove(element: V) = sourceList.remove(element!!.toJsonObjectData())

    override fun contains(element: V) = sourceList.contains(element!!.toJsonObjectData())

    override fun isEmpty() = sourceList.isEmpty()
    override fun iterator(): Iterator<V> = sourceList.readAll().map { it.toObject() as V }.iterator()

    override fun listIterator(): ListIterator<V> = sourceList.readAll().map { it.toObject() as V }.listIterator()

    override fun listIterator(index: Int): ListIterator<V> = sourceList.readAll().map { it.toObject() as V }.listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int): List<V> = sourceList.readAll().map { it.toObject() as V }.subList(fromIndex, toIndex)
    override fun lastIndexOf(element: V): Int = sourceList.readAll().map { it.toObject() as V }.lastIndexOf(element)

    fun addAll(elements: Collection<V>) = sourceList.addAll(elements.map { it!!.toJsonObjectData() })

    fun removeAll(elements: Collection<V>) = sourceList.removeAll(elements.map { it!!.toJsonObjectData() })

    fun clear() = sourceList.clear()

    override val size: Int get() = sourceList.size

    override fun containsAll(elements: Collection<V>): Boolean = sourceList.containsAll(elements.map { it!!.toJsonObjectData() })

    override fun get(index: Int): V = sourceList.get(index).toObject()
    override fun indexOf(element: V): Int = sourceList.readAll().map { it.toObject() as V }.indexOf(element)

    fun removeIf(filter: (V) -> Boolean) = sourceList.removeIf { filter(it.toObject()) }

    fun count(filter: (V) -> Boolean) = sourceList.count { filter(it.toObject()) }

    fun firstOrNull(filter: (V) -> Boolean) = sourceList.firstOrNull { filter(it.toObject()) }?.toObject() as V

}