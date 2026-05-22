package com.mackereldev.pallogv2.data.repository

import com.mackereldev.pallogv2.data.datasource.AssetDataSource
import com.mackereldev.pallogv2.data.model.Pal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PalRepository @Inject constructor(
    private val dataSource: AssetDataSource
) {
    private var cache: List<Pal>? = null

    fun getPals(): List<Pal> {
        if(cache == null) cache = dataSource.loadPals()
        return cache!!
    }

    fun getPal(deckIndex: Int): Pal? =
        getPals().find { it.palDeckIndex == deckIndex }
}