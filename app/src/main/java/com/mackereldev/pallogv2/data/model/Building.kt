package com.mackereldev.pallogv2.data.model

import com.google.gson.annotations.SerializedName

data class Building (
    val id: String = "",
    val name: String = "",
    val href: String = "",
    val imgurl: String = "",
    val description: String = "",
    @SerializedName("Type") val type: String = "",
    @SerializedName("Rank") val rank: String = "",
    @SerializedName("Code") val code: String = "",
    val 금화: String = ""
) {
    fun iconAssetPath(category: BuildingCategory): String {
        val fileName = imgurl.substringAfterLast("/")
        return "file:///android_asset/PAL/Icon/${category.iconFolder}/$fileName"
    }
}

enum class BuildingCategory(
    val displayName: String,
    val fileName: String,
    val iconFolder: String
) {
    PRODUCTION( "생산",   "Production.json",      "images_production"),
    PAL(        "팰",     "PalArchitecture.json", "images_palArchitecture"),
    STORAGE(    "수납",   "Storage.json",          "images_storage"),
    FOOD(       "식료품", "FoodArchitecture.json", "images_foodArchitecture"),
    INFRA(      "인프라", "Infra.json",            "images_infra"),
    LIGHTING(   "조명",   "Lighting.json",         "images_lighting"),
    FOUNDATION( "건축",   "Foundation.json",       "images_foundation"),
    DEFENSES(   "방어",   "Defenses.json",         "images_defenses"),
    OTHER(      "기타",   "Other.json",            "images_other"),
    FURNITURE(  "가구",   "Furniture.json",        "images_furniture"),
    SCHEMATICS( "설계도", "Schematics.json",       "images_schematics")
}

