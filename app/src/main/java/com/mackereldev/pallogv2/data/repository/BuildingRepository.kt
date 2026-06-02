package com.mackereldev.pallogv2.data.repository

import com.mackereldev.pallogv2.data.datasource.AssetDataSource
import com.mackereldev.pallogv2.data.model.Building
import com.mackereldev.pallogv2.data.model.BuildingCategory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildingRepository @Inject constructor(
    private val dataSource: AssetDataSource
) {
    private val cache = mutableMapOf<BuildingCategory, List<Building>>()

    fun getBuildings(category: BuildingCategory): List<Building> {
        return cache.getOrPut(category) {
            dataSource.loadBuildings(category.fileName)
        }
    }
}