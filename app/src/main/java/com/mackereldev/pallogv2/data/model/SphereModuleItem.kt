package com.mackereldev.pallogv2.data.model

import com.google.gson.annotations.SerializedName

data class SphereModuleItem(
    val id: String = "",
    val name: String = "",
    val href: String = "",
    val imgurl: String = "",
    val production: List<ProductionFacility> = emptyList(),
    val description: String = "",
    val dynamic: Map<String, String> = emptyMap(),
    val skill: List<String> = emptyList(),
    @SerializedName("meterial") val material: Map<String, String> = emptyMap(),
    @SerializedName("Rarity") val rarity: String = "",
    @SerializedName("금화") val price: String = "",
    @SerializedName("Weight") val weight: String = "",
    @SerializedName("MaxStackCount") val maxStackCount: String = "",
    @SerializedName("SneakAttackRate") val sneakAttackRate: String = "",
    @SerializedName("Code") val code: String = ""
) {
    fun iconAssetPath(category: ItemCategory): String {
        val fileName = imgurl.substringAfterLast("/")
        return "file:///android_asset/PAL/Icon/${category.iconFolder}/$fileName"
    }

    val techLevel: String
        get() = dynamic["기술"] ?: ""

    // "포획력 강화 +N" 문구
    val captureBonus: String?
        get() = skill.firstOrNull { it.contains("포획력") }

    // 모듈별 특성 문구 (커브/비거리/슬라이더/호밍 등)
    val trait: String?
        get() = skill.firstOrNull { !it.contains("포획력") }
}

