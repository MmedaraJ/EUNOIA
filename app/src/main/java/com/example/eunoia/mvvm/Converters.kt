package com.example.eunoia.mvvm

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

public class Converters {
    /**
     * Convert a a list of Uris to a Json
     */
    @TypeConverter
    fun convertUriListToJson(uris: List<Uri>): String {
        return Gson().toJson(uris)
    }

    /**
     * Convert a json to a list of Uris
     */
    @TypeConverter
    fun getUriListFromJson(jsonUris: String): List<Uri> {
        return Gson().fromJson(
            jsonUris,
            object : TypeToken<List<Uri>>() {}.type
        )
    }

    @TypeConverter
    fun convertIntListToJson(ints: List<Int>): String {
        return Gson().toJson(ints)
    }

    @TypeConverter
    fun getIntListFromJson(jsonStrings: String): List<Int> {
        return Gson().fromJson(
            jsonStrings,
            object : TypeToken<List<Int>>() {}.type
        )
    }

    @TypeConverter
    fun convertStringListToJson(strings: List<String>): String {
        return Gson().toJson(strings)
    }

    @TypeConverter
    fun getStringListFromJson(jsonStrings: String): List<String> {
        return Gson().fromJson(
            jsonStrings,
            object : TypeToken<List<String>>() {}.type
        )
    }
}