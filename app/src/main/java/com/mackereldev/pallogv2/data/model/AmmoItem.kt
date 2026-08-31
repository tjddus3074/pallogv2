package com.mackereldev.pallogv2.data.model

import com.google.gson.annotations.SerializedName

data class AmmoItem(
    val id: String = "",
    val name: String = "",
    val href: String = "",
    val imgurl: String = "",
    val dynamic: Map<String, String> = emptyMap(),
    val description: String = "",
    @SerializedName("meterial") val meterial: Map<String, String> = emptyMap(),
    val production: List<ProductionFacility> = emptyList(),
    @SerializedName("Rarity") val rarity: String = "",
    @SerializedName("Type") val type: String = "",
    @SerializedName("Rank") val rank: String = "",
    val 금화: String = "",
    @SerializedName("Weight") val weight: String = "",
    @SerializedName("MaxStackCount") val maxStackCount: String = "",
    @SerializedName("Code") val code: String = ""
) {
    fun iconAssetPath(category: ItemCategory): String {
        val fileName = imgurl.substringAfterLast("/")
        return "file:///android_asset/PAL/Icon/${category.iconFolder}/$fileName"
    }

    val techLevel: String
        get() = dynamic["기술"] ?: ""
}
