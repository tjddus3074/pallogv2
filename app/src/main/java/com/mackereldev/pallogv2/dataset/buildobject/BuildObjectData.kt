package com.mackereldev.pallogv2.dataset.buildobject

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.mackereldev.pallogv2.dataset.GetData
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildObjectData @Inject constructor(
    @ApplicationContext private val context: Context
) {

    @Inject lateinit var getData: GetData

    // 건축물 데이터 목록
    private val _buildObjectDataList by lazy { getData.readJsonCommon("PAL/DataTable/MapObejct/Building/DT_BuildObjectDataTable_Common.json") }
    fun buildObjectDataList() = _buildObjectDataList

    // 건축물 카테고리 목록
    private val _buildObjectCategoryKo by lazy { getData.readJsonCommon("PAL/ko/Pal/DataTable/Text/DT_BuildObjectCategoryText.json") }
    fun buildObjectCategoryKo() = _buildObjectCategoryKo

    // 건축물 설명
    private val _buildObjectDescriptionKo by lazy { getData.readJsonCommon("PAL/ko/Pal/DataTable/Text/DT_BuildObjectDescText_Common.json") }
    fun buildObjectDescriptionKo() = _buildObjectDescriptionKo

    // 건축물 이름
    private val _buildObjectNameKo by lazy { getData.readJsonCommon("PAL/ko/Pal/DataTable/Text/DT_MapObjectNameText_Common.json") }
    fun buildObjectNameKo() = _buildObjectNameKo

    // 건축물 이름 한글
    fun buildObjectNameKoList(): Map<String, Any> {
        return _buildObjectNameKo
            .mapKeys { (key, _) -> key }
            .mapValues { (_, value) ->
                (value as JSONObject).getJSONObject("TextData").getString("LocalizedString")
            }
    }

    // 건축물 이미지
    private val bitmapCache = mutableMapOf<String, Bitmap?>()
    @Composable
    fun buildPng(name: String, modifier: Modifier = Modifier) {
        val bitmap = remember(name) {
            bitmapCache.getOrPut(name) {
                val buildObject = _buildObjectDataList[name] as? JSONObject
                val path = "PAL/Texture/BuildObject/PNG/T_icon_buildObject_${name}.png"
                try {
                    context.assets.open(path).use { BitmapFactory.decodeStream(it) }
                } catch (e: Exception) { null }
            }
        }
    }
}