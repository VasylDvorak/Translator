package com.translator.utils.network

import android.content.SharedPreferences
import com.google.gson.Gson
import com.translator.model.data.DataModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPreferencesDelegate(
    private val preferences: SharedPreferences,
    private val name: String
) : ReadWriteProperty<Any?, List<DataModel>?> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): List<DataModel> {
        val jsonStringList = preferences.getString(name, "[]") ?: "[]"
        return Gson().fromJson(jsonStringList, Array<DataModel>::class.java).asList()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: List<DataModel>?) {
        val jsonStr = Gson().toJson(value)
        preferences.edit()?.putString(name, jsonStr)?.apply()
    }
}