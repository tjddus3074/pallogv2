package com.mackereldev.pallogv2.data.repository

import com.mackereldev.pallogv2.data.datasource.AssetDataSource
import com.mackereldev.pallogv2.data.model.Building
import com.mackereldev.pallogv2.data.model.BuildingCategory
import com.mackereldev.pallogv2.data.model.FacilityLocation
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

    fun getBuilding(category: BuildingCategory, href: String): Building? =
        getBuildings(category).find { it.href == href }

    // 생산 시설 이름으로 모든 건축물 카테고리를 뒤져 실제 소속 카테고리/href/아이콘을 찾음
    // (제작 재료의 생산 시설은 "생산" 카테고리에만 있는 게 아니라 팰/식료품/인프라 등에도 걸쳐 있음)
    fun findFacilityLocation(name: String): FacilityLocation? {
        for (category in BuildingCategory.entries) {
            getBuildings(category).find { it.name == name }?.let {
                return FacilityLocation(it.iconAssetPath(category), category, it.href)
            }
        }
        return null
    }
}