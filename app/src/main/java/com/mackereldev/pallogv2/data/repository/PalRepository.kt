package com.mackereldev.pallogv2.data.repository

import com.mackereldev.pallogv2.data.datasource.AssetDataSource
import com.mackereldev.pallogv2.data.model.Pal
import com.mackereldev.pallogv2.data.model.PalHabitat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PalRepository @Inject constructor(
    private val dataSource: AssetDataSource
) {
    private var cache: List<Pal>? = null
    private val habitatCache = mutableMapOf<String, PalHabitat?>()

    fun getPals(): List<Pal> {
        if(cache == null) cache = dataSource.loadPals().sortedBy { it.palDeckIndex }
        return cache!!
    }

    fun getPal(code: String): Pal? =
        getPals().find { it.stats.code == code }

    fun getPalHabitat(code: String): PalHabitat? =
        habitatCache.getOrPut(code) { dataSource.loadPalHabitat(code) }
}
