package com.mackereldev.pallogv2.dataset.pal

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import com.mackereldev.pallogv2.dataset.GetData
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PalData @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getData: GetData
) {

    private val _paldata by lazy { getData.readJsonCommon("PAL/DataTable/Character/DT_PalMonsterParameter.json").toMutableMap() }
    private val _palicon by lazy { getData.readJsonCommon("PAL/DataTable/Character/DT_PalCharacterIconDataTable_Common.json")
        .mapValues { (_, v) -> (v as JSONObject).getJSONObject("Icon").getString("AssetPathName") } }
    private val _palko by lazy { getData.readJsonCommon("PAL/ko/Pal/DataTable/Text/DT_PalNameText_Common.json") }

    //팰 데이터
    fun palDataList(): Map<String, Any> {
        val result = _paldata
        val result2 = _palicon
        val palko = _palko

        val keysToRemove = result.keys.filter { key ->
            key.contains("BOSS") || key.contains("GYM") ||
                    key.contains("RAID") || key.contains("Boss") ||
                    result2[key].toString() == "None" || palko["PAL_NAME_" + key].toString() == "ko_Text" ||
                    palko["PAL_NAME_" + key] == null
        }
        keysToRemove.forEach { result.remove(it) }

        return result.entries
            .sortedBy { (_, value) -> (value as JSONObject).getInt("ZukanIndex") }
            .associate { (key, value) -> key to value }
    }

    //팰 이름
    fun palDataKoList(): Map<String, Any> {
        return _palko
            .mapKeys { (key, _) -> key.lowercase() }
            .mapValues { (_, value) ->
                (value as JSONObject).getJSONObject("TextData").getString("LocalizedString")
            }
    }

    // 팰 이미지
    @Composable fun palPng(name: String, modifier: Modifier = Modifier) {
        val pngpath = if (name.contains("Yakushima")) {
            "PAL/Texture/PalIcon/Normal/Yakushima/T_${name}_icon_normal.png"
        } else {
            "PAL/Texture/PalIcon/Normal/T_${name}_icon_normal.png"
        }
        if(getData.assetExists(pngpath)) {
            val bitmap = remember {
                context.assets.open(pngpath).use { BitmapFactory.decodeStream(it) }
            }
            Image(bitmap = bitmap.asImageBitmap(), contentDescription = null, modifier = modifier)
        }
    }
}