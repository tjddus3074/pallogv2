package com.mackereldev.pallogv2.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serial

data class SphereItem(
    val id: String = "",
    val name: String = "",
    val href : String = "",
    val imgurl: String = "",
    val levels: List<SphereLevel> = emptyList(),
    val production: List<ProductionFacility> = emptyList(),
    // 단일 스펙 스피어는 levels없이 최상위에 dynamic/Rarity가 있음
    val description: String = "",
    val dynamic: Map<String, String> = emptyMap(),
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

    val effectiveLevels: List<SphereLevel>
        get() = levels.ifEmpty {
            listOf(
                SphereLevel(
                    description = description,
                    dynamic = dynamic,
                    material = material,
                    rarity = rarity,
                    price = price,
                    weight = weight,
                    maxStackCount = maxStackCount,
                    sneakAttackRate = sneakAttackRate,
                    code = code
                )
            )
        }

    private val baseLevel: SphereLevel
        get() = effectiveLevels.firstOrNull() ?: SphereLevel()

    val rarities: List<String>
        get() = effectiveLevels.mapNotNull { it.rarity.ifBlank { null } }.distinct()

    val captureRate: String
        get() = baseLevel.dynamic["포획력"] ?: ""

    val techLevel: String
        get() = baseLevel.dynamic["기술"] ?: ""
}

data class SphereLevel(
    val description: String = "",
    val dynamic: Map<String, String> = emptyMap(),
    @SerializedName("meterial") val material: Map<String, String> = emptyMap(),
    @SerializedName("Rarity") val rarity: String = "",
    @SerializedName("금화") val price: String = "",
    @SerializedName("Weight") val weight: String = "",
    @SerializedName("MaxStackCount") val maxStackCount: String = "",
    @SerializedName("SneakAttackRate") val sneakAttackRate: String = "",
    @SerializedName("Code") val code: String = ""
) {
    val captureRate: String get() = dynamic["포획력"] ?: ""
    val techLevel: String get() = dynamic["기술"] ?: ""
}
