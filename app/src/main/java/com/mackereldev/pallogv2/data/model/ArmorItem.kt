package com.mackereldev.pallogv2.data.model

import com.google.gson.annotations.SerializedName

data class ArmorItem(
    val id: String = "",
    val name: String = "",
    val href: String = "",
    val imgurl: String = "",
    val levels: List<ArmorLevel> = emptyList(),
    val production: List<ProductionFacility> = emptyList(),
    // 방패류 등 단일 등급 방어구는 levels 없이 최상위에 스펙이 바로 들어있음
    val description: String = "",
    val dynamic: Map<String, String> = emptyMap(),
    @SerializedName("meterial") val material: Map<String, String> = emptyMap(),
    @SerializedName("Rarity") val rarity: String = "",
    @SerializedName("금화") val price: String = "",
    @SerializedName("Weight") val weight: String = "",
    @SerializedName("내구도") val durability: String = "",
    @SerializedName("MaxStackCount") val maxStackCount: String = "",
    @SerializedName("SneakAttackRate") val sneakAttackRate: String = "",
    @SerializedName("Code") val code: String = ""
) {
    fun iconAssetPath(category: ItemCategory): String {
        val fileName = imgurl.substringAfterLast("/")
        return "file:///android_asset/PAL/Icon/${category.iconFolder}/$fileName"
    }

    val effectiveLevels: List<ArmorLevel>
        get() = levels.ifEmpty {
            listOf(
                ArmorLevel(
                    description = description,
                    dynamic = dynamic,
                    material = material,
                    rarity = rarity,
                    price = price,
                    weight = weight,
                    durability = durability,
                    maxStackCount = maxStackCount,
                    sneakAttackRate = sneakAttackRate,
                    code = code
                )
            )
        }

    // 등급형 방어구는 levels[0], 방패처럼 단일 스펙 아이템은 최상위 값으로 합성
    private val baseLevel: ArmorLevel
        get() = effectiveLevels.firstOrNull() ?: ArmorLevel()

    val rarities: List<String>
        get() = effectiveLevels.mapNotNull { it.rarity.ifBlank { null } }.distinct()

    val isShield: Boolean
        get() = baseLevel.dynamic.containsKey("방패")

    val shield: String
        get() = baseLevel.dynamic["방패"] ?: ""

    val hp: String
        get() = baseLevel.dynamic["HP"] ?: ""

    val defense: String
        get() = baseLevel.dynamic["방어"] ?: ""

    val techLevel: String
        get() = baseLevel.dynamic["기술"] ?: ""
}

data class ArmorLevel(
    val description: String = "",
    val dynamic: Map<String, String> = emptyMap(),
    @SerializedName("meterial") val material: Map<String, String> = emptyMap(),
    @SerializedName("Rarity") val rarity: String = "",
    @SerializedName("금화") val price: String = "",
    @SerializedName("Weight") val weight: String = "",
    @SerializedName("내구도") val durability: String = "",
    @SerializedName("MaxStackCount") val maxStackCount: String = "",
    @SerializedName("SneakAttackRate") val sneakAttackRate: String = "",
    @SerializedName("Code") val code: String = ""
) {
    val isShield: Boolean get() = dynamic.containsKey("방패")
    val shield: String get() = dynamic["방패"] ?: ""
    val hp: String get() = dynamic["HP"] ?: ""
    val defense: String get() = dynamic["방어"] ?: ""
    val techLevel: String get() = dynamic["기술"] ?: ""
}