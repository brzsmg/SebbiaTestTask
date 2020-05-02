package com.sebbia.brzsmg.testtask

import com.google.gson.*
import com.sebbia.brzsmg.testtask.types.DateTime
//import com.swlibs.common.types.DateTime
import java.lang.reflect.Type

/**
 * Обычно сдесь Gson или Moshi.
 */
object Json {
    private var mGson: Gson? = null
    private fun init() {
        if (mGson != null) {
            return
        }
        mGson = GsonBuilder()
            .registerTypeAdapter(
                Float::class.java,
                JsonDeserializer { jsonElement, type, jsonDeserializationContext ->
                    var result: Float? = null
                    result = try {
                        jsonElement.asFloat
                    } catch (e: NumberFormatException) {
                        return@JsonDeserializer result
                    }
                    result
                })
            .registerTypeAdapter(
                DateTime::class.java,
                JsonSerializer<Any> { json, typeOfSrc, context -> JsonPrimitive(json.toString()) })
            .registerTypeAdapter(DateTime::class.java,
                JsonDeserializer<Any> { json, typeOfT, context -> DateTime(json.asString) }) //.registerTypeAdapter(DateTime.class, new DateTimeAdapter())
            .setLenient()
            .create()
    }

    val gson: Gson?
        get() {
            init()
            return mGson
        }

    fun <T> fromJson(json: String?, c: Class<T>?): T {
        init()
        return mGson!!.fromJson(json, c)
    }

    fun <T> fromJson(json: String?, c: Type?): T {
        init()
        return mGson!!.fromJson(json, c)
    }

    fun <T> toJson(o: T): String {
        init()
        return mGson!!.toJson(o)
    }
}