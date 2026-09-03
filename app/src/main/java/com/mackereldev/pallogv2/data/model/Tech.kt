package com.mackereldev.pallogv2.data.model

data class TechItem(
    val itemName: String = "",
    val dataHover: String = "",
    val isBossTechnology: Boolean = false,
    val imgurl: String = ""
) {
    val iconAssetPath: String
        get() = "file:///android_asset/PAL/Icon/images_tech/${imgurl.substringAfterLast("/")}"
}

data class TechRow(
    val level: String,
    val items: List<TechItem>
)
