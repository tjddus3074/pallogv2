package com.mackereldev.pallogv2.data.model

import com.google.gson.annotations.SerializedName

data class Item (
    val id: String = "",
    val name: String = "",
    val href: String = "",
    val imgurl: String = "",
    val description: String = "",
    @SerializedName("Rarity") val rarity: String = "",
    @SerializedName("Type") val type: String = "",
    @SerializedName("Rank") val rank: String = "",
    @SerializedName("Code") val code: String = "",
    val 금화: String = "",
    @SerializedName("Weight") val weight: String = "",
    @SerializedName("MaxStackCount") val maxStackCount: String = "",
    @SerializedName("SneakAttackRate") val sneakAttackRate: String = "",
    @SerializedName("meterial") val material: Map<String, String> = emptyMap(),
    val production: List<ProductionFacility> = emptyList(),
    val dynamic: Map<String, String> = emptyMap()
) {

    fun iconAssetPath(category: ItemCategory) : String {
        val fileName = imgurl.substringAfterLast("/")
        return "file:///android_asset/PAL/Icon/${category.iconFolder}/$fileName"
    }

    val techLevel: String
        get() = dynamic["기술"] ?: ""
}

enum class ItemCategory(val displayName: String, val fileName: String, val iconFolder: String) {
    WEAPON("무기", "Weapon.json", "images_weapon"),
    AMMO("탄약", "Ammo.json", "images_ammo"),
    ARMOR("방어구", "Armor.json", "images_armor"),
    SPHERE("스피어", "Sphere.json", "images_sphere"),
    SPHERE_MODULE("스피어 모듈", "Sphere_Module.json", "images_sphere_module"),
    ACCESSORY("장신구", "Accessory.json", "images_accessory"),
    MATERIAL("소재", "Material.json", "images_material"),
    CONSUMABLE("소모품", "Consumable.json", "images_consumable"),
    INGREDIENT("식재료", "Ingredient.json", "images_ingredient"),
    KEY_ITEM("귀중품", "KeyItem.json", "images_keyitem")

}
