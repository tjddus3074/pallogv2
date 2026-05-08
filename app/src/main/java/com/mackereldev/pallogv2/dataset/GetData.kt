package com.mackereldev.pallogv2.dataset

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONArray
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetData @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // 공통 제이슨 파일 리더
    fun readJsonCommon(path: String): Map<String, Any> {
        val json = context.assets
            .open(path)
            .bufferedReader()
            .use { it.readText() }

        val root = JSONArray(json)
        val rows = root.getJSONObject(0).getJSONObject("Rows")
        val result = mutableMapOf<String, Any>()
        rows.keys().forEach { key ->
            result[key] = rows.getJSONObject(key)
        }
        return result
    }

    // 파일 존재 여부
    fun assetExists(path: String): Boolean {
        return try {
            context.assets.open(path).close()
            true
        } catch (e: IOException) {
            false
        }
    }

}