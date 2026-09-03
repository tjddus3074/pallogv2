package com.mackereldev.pallogv2.data.repository

import com.mackereldev.pallogv2.data.datasource.AssetDataSource
import com.mackereldev.pallogv2.data.model.BuildingCategory
import com.mackereldev.pallogv2.data.model.ItemCategory
import com.mackereldev.pallogv2.data.model.TechRow
import javax.inject.Inject
import javax.inject.Singleton

// 기술 아이템 터치 시 이동할 상세 화면 종류
sealed class TechNavTarget {
    data class Item(val category: ItemCategory, val href: String) : TechNavTarget()
    data class Production(val href: String) : TechNavTarget()
    data class BuildingSuitability(val category: BuildingCategory, val href: String) : TechNavTarget()
    data class Storage(val href: String) : TechNavTarget()
    data class Schematics(val href: String) : TechNavTarget()
}

@Singleton
class TechRepository @Inject constructor(
    private val dataSource: AssetDataSource,
    private val itemRepository: ItemRepository,
    private val buildingRepository: BuildingRepository
) {
    private var cache: List<TechRow>? = null
    private var navIndexCache: Map<String, TechNavTarget>? = null

    fun getTechRows(): List<TechRow> {
        if (cache == null) cache = dataSource.loadTechRows()
        return cache!!
    }

    // 기술 아이템 이름(itemName) -> 상세 화면. 상세 화면이 없는 카테고리(건축 세트 등)는 매핑에서 빠져 자연히 null.
    fun resolveTarget(itemName: String): TechNavTarget? = getNavIndex()[itemName]

    private fun getNavIndex(): Map<String, TechNavTarget> {
        navIndexCache?.let { return it }

        val index = mutableMapOf<String, TechNavTarget>()

        listOf(
            ItemCategory.WEAPON, ItemCategory.AMMO, ItemCategory.ARMOR,
            ItemCategory.SPHERE, ItemCategory.SPHERE_MODULE, ItemCategory.ACCESSORY,
            ItemCategory.MATERIAL, ItemCategory.CONSUMABLE, ItemCategory.INGREDIENT,
            ItemCategory.KEY_ITEM
        ).forEach { category ->
            itemRepository.getItems(category).forEach { item ->
                index.putIfAbsent(item.name, TechNavTarget.Item(category, item.href))
            }
        }

        buildingRepository.getBuildings(BuildingCategory.PRODUCTION).forEach { building ->
            index.putIfAbsent(building.name, TechNavTarget.Production(building.href))
        }
        listOf(BuildingCategory.PAL, BuildingCategory.FOOD, BuildingCategory.INFRA, BuildingCategory.LIGHTING)
            .forEach { category ->
                buildingRepository.getBuildings(category).forEach { building ->
                    index.putIfAbsent(building.name, TechNavTarget.BuildingSuitability(category, building.href))
                }
            }
        buildingRepository.getBuildings(BuildingCategory.STORAGE).forEach { building ->
            index.putIfAbsent(building.name, TechNavTarget.Storage(building.href))
        }
        buildingRepository.getBuildings(BuildingCategory.SCHEMATICS).forEach { building ->
            index.putIfAbsent(building.name, TechNavTarget.Schematics(building.href))
        }

        navIndexCache = index
        return index
    }
}