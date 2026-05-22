package com.mackereldev.pallogv2.data.datasource

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mackereldev.pallogv2.data.model.Pal
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun loadPals() : List<Pal> {
        val json = context.assets.open("PAL/Json/pals_data.json")
            .bufferedReader()
            .use { it.readText() }
        return Gson().fromJson(json, object : TypeToken<List<Pal>>() {}.type)
    }
}