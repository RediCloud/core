package net.dustrean.api.utils.extension

import com.google.gson.Gson


val gson = Gson()

data class JsonObjectData(val json: String, val clazz: String)

fun <T> JsonObjectData.toObject(): T = gson.fromJson(json, Class.forName(clazz)) as T

inline fun <reified T> T.toJsonObjectData(): JsonObjectData = JsonObjectData(gson.toJson(this), T::class.java.name)