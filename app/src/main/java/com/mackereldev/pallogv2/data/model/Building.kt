package com.mackereldev.pallogv2.data.model

import com.google.gson.annotations.SerializedName

data class Building (
    val id: String = "",
    val name: String = "",
    val href: String = "",
    val imgurl: String = "",
    val description: String = "",
    val dynamic: Map<String, String> = emptyMap(),
    @SerializedName("meterial") val material: Map<String, String> = emptyMap(),
    val production: List<ProductionFacility> = emptyList(),
    @SerializedName("요구적성") val requiredSuitability: List<RequiredSuitability> = emptyList(),
    @SerializedName("작업타입") val buildWorkType: String = "",
    @SerializedName("작업량") val buildWorkAmount: String = "",
    @SerializedName("Rarity") val rarity: String = "",
    @SerializedName("MaxStackCount") val maxStackCount: String = "",
    @SerializedName("Type") val type: String = "",
    @SerializedName("Rank") val rank: String = "",
    @SerializedName("Code") val code: String = "",
    val 금화: String = ""
) {
    fun iconAssetPath(category: BuildingCategory): String {
        val fileName = imgurl.substringAfterLast("/")
        return "file:///android_asset/PAL/Icon/${category.iconFolder}/$fileName"
    }

    val techLevel: String get() = dynamic["기술"] ?: ""
    val requiredSan: String get() = dynamic["SAN"] ?: ""
    val slots: String get() = dynamic["Slots"] ?: ""
}

data class FacilityLocation(
    val iconPath: String,
    val category: BuildingCategory,
    val href: String
)

// 건물 운용에 필요한 팰 작업 적성 (요구적성 배열의 원소)
data class RequiredSuitability(
    @SerializedName("type") val type: String = "",
    val name: String = "",
    val imgurl: String = "",
    val level: String = ""
) {
    val koreanLabel: String get() = needWorkToKorean(type)

    // imgurl 파일명을 그대로 로컬 에셋 경로로 사용 (PalCard의 worktoimgpath 결과와 같은 명명 규칙)
    val localIconPath: String
        get() {
            val fileName = imgurl.substringAfterLast("/").substringBeforeLast(".")
            return "file:///android_asset/PAL/Texture/UI/InGame/$fileName.png"
        }
}

// 영문 작업 적성 코드 -> 한글명 (팰 작업 적성과 동일한 이름 체계)
fun needWorkToKorean(code: String): String = when (code) {
    "Handiwork" -> "수작업"
    "Mining" -> "채굴"
    "Lumbering" -> "벌목"
    "Watering" -> "관개"
    "Kindling" -> "불 피우기"
    "Medicine_Production" -> "제약"
    "Cooling" -> "냉각"
    "Gathering" -> "채집"
    "Planting" -> "파종"
    "Generating_Electricity" -> "발전"
    "Transporting" -> "운반"
    "Farming" -> "목장"
    "All" -> "모든 작업"
    "" -> ""
    else -> code
}

enum class BuildingCategory(
    val displayName: String,
    val fileName: String,
    val iconFolder: String
) {
    PRODUCTION( "생산",   "Production.json",      "images_production"),
    PAL(        "팰",     "PalArchitecture.json", "images_palarch"),
    STORAGE(    "수납",   "Storage.json",          "images_storage"),
    FOOD(       "식료품", "FoodArchitecture.json", "images_foodarch"),
    INFRA(      "인프라", "Infra.json",            "images_infra"),
    LIGHTING(   "조명",   "Lighting.json",         "images_lighting"),
    FOUNDATION( "건축",   "Foundation.json",       "images_foundation"),
    DEFENSES(   "방어",   "Defenses.json",         "images_defenses"),
    OTHER(      "기타",   "Other.json",            "images_other"),
    FURNITURE(  "가구",   "Furniture.json",        "images_furniture"),
    SCHEMATICS( "설계도", "Schematics.json",       "images_schematics")
}
