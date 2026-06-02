package com.mackereldev.pallogv2.data.model

import com.google.gson.annotations.SerializedName

data class Pal (
    val id: String,
    val palname: String,
    val image: String,
    val description: String,
    val isVariety: String,
    val palDeckIndex: Int,
    val types: List<String>,
    val stats: PalStats,
    val partnerSkill: String,
    val partnerSkillDescription: String,
    val partnerSkillImage: String,
    val skills: List<PalSkill>,
    val suitability: Map<String, String>,
    val foodamount: Int,
    val drops: List<PalDrop>
) {
    val iconAssetpath: String
        get() = if (stats.code.contains("Yakushima")) {
            "PAL/Texture/PalIcon/Normal/Yakushima/T_${stats.code}_icon_normal.png"
        } else {
            "PAL/Texture/PalIcon/Normal/T_${stats.code}_icon_normal.png"
        }

}

data class PalStats(
    @SerializedName("Size") val size: String = "",
    @SerializedName("Rarity") val rarity: String = "",
    @SerializedName("HP") val hp: String = "",
    @SerializedName("식사량") val hunger: String = "",
    @SerializedName("MeleeAttack") val meleeAttack: String = "",
    @SerializedName("공격") val attack: String = "",
    @SerializedName("방어") val defense: String = "",
    @SerializedName("작업속도") val workSpeed: String = "",
    @SerializedName("Support") val support: String = "",
    @SerializedName("CaptureRateCorrect") val captureRate: String = "",
    @SerializedName("MaleProbability") val maleProbability: String = "",
    @SerializedName("CombiRank") val combiRank: String = "",
    @SerializedName("금화") val price: String = "",
    @SerializedName("Egg") val egg: String = "",
    @SerializedName("Code") val code: String = ""
)

data class PalSkill(
    val skillname: String,
    val skilllevel: String,
    val skilldescription: String,
    val skilltype: String,
    val cooltime: String,
    val power: String
)

data class PalDrop(
    val dropitem: String,
    val quantity: String,
    val dropprobability: String
)