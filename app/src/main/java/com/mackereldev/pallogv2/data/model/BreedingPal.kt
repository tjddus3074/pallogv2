package com.mackereldev.pallogv2.data.model

data class BreedingPalData(
    val pals: Map<String, BreedingPal> = emptyMap()
)

data class BreedingPal(
    val id: String = "",
    val no: String = "",
    val sortIndex: Int = 0,
    val name: String = "",
    val breedingPower: Int = 0,
    val tiebreakIndex: Int = 0,
    val maleProbability: Int = 0,
    val combiDuplicatePriority: Int = 0
)

data class UniqueComboEntry(
    val childCode: String,
    val parent1Code: String,
    val parent1Gender: String,
    val parent2Code: String,
    val parent2Gender: String
)
