package com.mackereldev.pallogv2.data.model

data class PalHabitat(
    val id: String = "",
    val palname: String = "",
    val code: String = "",
    val dayTimeLocations: List<HabitatLocation> = emptyList(),
    val nightTimeLocations: List<HabitatLocation> = emptyList()
)

data class HabitatLocation(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val lv: Int = 0,
    val map: String = "",
    val pixelX: Int = 0,
    val pixelY: Int = 0,
    val tileX: Int = 0,
    val tileY: Int = 0
)
