package com.mackereldev.pallogv2.data.model

import com.google.gson.annotations.SerializedName

data class AccessoryItem(
    val id: String = "",
    val name: String = "",
    val href: String = "",
    val imgurl: String = "",
    val levels: List<AccessoryLevel> = emptyList(),
    val production: List<ProductionFacility> = emptyList(),
    // 단일 스펙 장신구는 levels 없이 최상위에 스펙이 바로 들어있음
    val description: String = "",
    val dynamic: Map<String, String> = emptyMap(),
    val skill: String = "",
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

    // levels는 같은 아이템의 획득처(캐시 등) 중복 목록이라 대표 스펙(첫 항목)만 사용
    val effectiveLevels: List<AccessoryLevel>
        get() = levels.ifEmpty {
            listOf(
                AccessoryLevel(
                    description = description,
                    dynamic = dynamic,
                    skill = skill,
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

    private val baseLevel: AccessoryLevel
        get() = effectiveLevels.firstOrNull() ?: AccessoryLevel()

    val effect: String
        get() = baseLevel.skill.ifBlank { skill }
}

data class AccessoryLevel(
    val description: String = "",
    val dynamic: Map<String, String> = emptyMap(),
    val skill: String = "",
    @SerializedName("meterial") val material: Map<String, String> = emptyMap(),
    @SerializedName("Rarity") val rarity: String = "",
    @SerializedName("금화") val price: String = "",
    @SerializedName("Weight") val weight: String = "",
    @SerializedName("MaxStackCount") val maxStackCount: String = "",
    @SerializedName("SneakAttackRate") val sneakAttackRate: String = "",
    @SerializedName("Code") val code: String = ""
) {
    val techLevel: String get() = dynamic["기술"] ?: ""
}
