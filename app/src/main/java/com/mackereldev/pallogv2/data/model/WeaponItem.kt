package com.mackereldev.pallogv2.data.model

import com.google.gson.annotations.SerializedName

data class WeaponItem(
    val id: String = "",
    val name: String = "",
    val href: String = "",
    val imgurl: String = "",
    val levels: List<WeaponLevel> = emptyList(),
    val production: List<ProductionFacility> = emptyList(),
    val description: String = "",
    val dynamic: Map<String, String> = emptyMap(),
    val meterial: Map<String, String> = emptyMap(),
    @SerializedName("Rarity") val rarity: String = "",
    @SerializedName("공격") val attack: String = "",
    @SerializedName("금화") val price: String = "",
    @SerializedName("Weight") val weight: String = "",
    @SerializedName("내구도") val durability: String = "",
    @SerializedName("MaxStackCount") val maxStackCount: String = "",
    @SerializedName("MagazineSize") val magazineSize: String = "",
    @SerializedName("SneakAttackRate") val sneakAttackRate: String = "",
    @SerializedName("Code") val code: String = "",
    @SerializedName("TypeB") val typeB: String = ""
) {
    fun iconAssetPath(category: ItemCategory): String {
        val fileName = imgurl.substringAfterLast("/")
        return "file:///android_asset/PAL/Icon/${category.iconFolder}/$fileName"
    }

    val effectiveLevels: List<WeaponLevel>
        get() = levels.ifEmpty {
            listOf(
                WeaponLevel(
                    description = description,
                    dynamic = dynamic,
                    meterial = meterial,
                    rarity = rarity,
                    attack = attack,
                    price = price,
                    weight = weight,
                    durability = durability,
                    maxStackCount = maxStackCount,
                    magazineSize = magazineSize,
                    sneakAttackRate = sneakAttackRate,
                    code = code,
                    typeB = typeB
                )
            )
        }

    val baseLevel: WeaponLevel?
        get() = effectiveLevels.firstOrNull()

    val techLevel: String
        get() = baseLevel?.techLevel ?: ""

    val rarities: List<String>
        get() = effectiveLevels.mapNotNull { it.rarity.ifBlank { null } }.distinct()
}

data class ProductionFacility(
    val pname: String = "",
    val imgsrc: String = ""
)

data class WeaponLevel(
    val islevel: String = "",
    val description: String = "",
    val dynamic: Map<String, String> = emptyMap(),
    val meterial: Map<String, String> = emptyMap(), // 제작 재료 (재료명 -> 수량)
    @SerializedName("Rarity") val rarity: String = "",
    @SerializedName("공격") val attack: String = "",
    @SerializedName("금화") val price: String = "",
    @SerializedName("Weight") val weight: String = "",
    @SerializedName("내구도") val durability: String = "",
    @SerializedName("MaxStackCount") val maxStackCount: String = "",
    @SerializedName("MagazineSize") val magazineSize: String = "",   // 총기류만 존재
    @SerializedName("SneakAttackRate") val sneakAttackRate: String = "",
    @SerializedName("Code") val code: String = "",
    @SerializedName("TypeB") val typeB: String = ""
) {
    val techLevel: String
        get() = dynamic["기술"] ?: ""
}
