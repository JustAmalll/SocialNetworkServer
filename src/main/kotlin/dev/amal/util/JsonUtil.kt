package dev.amal.util

import com.google.gson.Gson

fun <T> Gson.fromJsonOrNull(json: String, clazz: Class<T>): T? =
    try {
        fromJson(json, clazz)
    } catch (e: Exception) {
        null
    }
