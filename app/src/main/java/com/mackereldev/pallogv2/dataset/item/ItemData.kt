package com.mackereldev.pallogv2.dataset.item

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
class ItemData @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getDate: GetData
) {

    //아이템 목록
    private val _itemList by lazy { getDate.readJsonCommon("PAL/DataTable/Item/DT_ItemDataTable_Common.json") }
    fun itemList() = _itemList

    //아이템 이름
    private val _itemNameKo by lazy { getDate.readJsonCommon("PAL/ko/Pal/DataTable/Text/DT_ItemNameText_Common.json") }
    fun itemNameKo() = _itemNameKo

    // 아이템 설명
    fun itemDescriptionKo() = getDate.readJsonCommon("PAL/ko/Pal/DataTable/Text/DB_ItemDescriptionText_Common.json")

    // 아이템 레시피 목록
    fun itemRecipe() = getDate.readJsonCommon("PAL/DataTable/Item/DT_ItemRecipeDataTable_Common.json")

    // 아이템 이름 한글
    fun itemDataKoList(): Map<String, Any> {
        return _itemNameKo
            .mapKeys { (key, _) -> key }
            .mapValues { (_, value) ->
                (value as JSONObject).getJSONObject("TextData").getString("LocalizedString")
            }
    }

    // 아이템 이미지
    private val bitmapCache = mutableMapOf<String, Bitmap?>()
    @Composable
    fun itemPng(name: String, modifier: Modifier = Modifier) {
        val bitmap = remember(name) {
            bitmapCache.getOrPut(name) {
                val item = _itemList[name] as? JSONObject
                val typeA = (item?.getString("TypeA") ?: "").substringAfterLast("::")
                val path = if(typeA.equals("BossDefeatReward") || typeA.equals("Jewelry")) {
                    "PAL/InventoryItemIcon/Texture/T_icon_item_${typeA}_${name}.png"
                } else {
                    "PAL/InventoryItemIcon/Texture/T_itemicon_${typeA}_${name}.png"
                }
                try {
                    context.assets.open(path).use { BitmapFactory.decodeStream(it) }
                } catch (e: Exception) { null }
            }
        }
        bitmap?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = modifier)
        }
    }

}