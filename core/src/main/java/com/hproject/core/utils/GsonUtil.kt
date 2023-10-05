package com.hproject.core.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object GsonUtil {
    private val internalBuilder = GsonBuilder()

    val base: Gson = internalBuilder.setPrettyPrinting().create()

    fun <T> listFromJson(string: String, type: Class<T>): List<T> =
        base.fromJson(string, TypeToken.getParameterized(List::class.java, type).type)

    fun <T> parameterizedFromJson(string: String, type1: Type, type2: Type): T =
        base.fromJson(string, TypeToken.getParameterized(type1, type2).type)

    fun <T> toJson(model: T): String = base.toJson(model)
    fun <T> toJsonObject(model: T): JsonObject = base.toJsonTree(model).asJsonObject
    fun <T> fromJson(string: String?, type1: Type): T? = base.fromJson(string, type1)
}