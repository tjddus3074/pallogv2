package com.mackereldev.pallogv2.data.repository

import com.mackereldev.pallogv2.data.breeding.PalBreedingLogic
import com.mackereldev.pallogv2.data.datasource.AssetDataSource
import com.mackereldev.pallogv2.data.model.Pal
import com.mackereldev.pallogv2.data.model.UniqueComboEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreedingRepository @Inject constructor(
    private val dataSource: AssetDataSource,
    private val palRepository: PalRepository
){
    private var logicCache: PalBreedingLogic? = null

    fun getLogic(): PalBreedingLogic {
        if(logicCache == null) {
            logicCache = PalBreedingLogic(dataSource.loadBreedingPals(), getUniqueCombos())
        }
        return logicCache!!
    }

    //pals_data.json의 uniqueCombo
    private fun getUniqueCombos() : List<UniqueComboEntry> =
        palRepository.getPals().mapNotNull { pal ->
            val combo = pal.uniqueCombo ?: return@mapNotNull null
            if(combo.parents.size < 2) return@mapNotNull null
            UniqueComboEntry(
                childCode = pal.stats.code,
                parent1Code = combo.parents[0].id,
                parent1Gender = combo.parents[0].gender,
                parent2Code = combo.parents[1].id,
                parent2Gender = combo.parents[1].gender
            )
        }

    fun getBreedablePals(): List<Pal> {
        val codes = getLogic().palCodes
        return palRepository.getPals().filter { it.stats.code in codes }
    }

    fun getPalsByCode(): Map<String, Pal> = palRepository.getPals().associateBy { it.stats.code }
}