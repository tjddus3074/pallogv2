package com.mackereldev.pallogv2.data.repository

import com.mackereldev.pallogv2.data.model.BuildingCategory
import com.mackereldev.pallogv2.data.model.ItemCategory
import com.mackereldev.pallogv2.data.model.Pal
import javax.inject.Inject
import javax.inject.Singleton

sealed class SearchNavTarget {
    data class PalDetail(val code: String) : SearchNavTarget()
    data class Item(val category: ItemCategory, val href: String) : SearchNavTarget()
    data class Building(val category: BuildingCategory, val href: String) : SearchNavTarget()
}

data class SearchResult(
    val name: String,
    val iconPath: String,
    val target: SearchNavTarget
)

@Singleton
class SearchRepository @Inject constructor(
    private val palRepository: PalRepository,
    private val itemRepository: ItemRepository,
    private val buildingRepository: BuildingRepository
) {
    private var indexCache: List<SearchResult>? = null

    fun search(query: String): List<SearchResult> {
        if (query.isBlank()) return emptyList()
        return getIndex().filter { it.name.contains(query, ignoreCase = true)}
    }

    // minSuitability: "수작업" -> 3 처럼 작업 적성별 최소 기본 레벨. 전부 만족해야 함(AND).
    // types: 선택한 속성 중 하나라도 포함하면 통과(OR). 비어있으면 조건 없음.
    fun filterPals(minSuitability: Map<String, Int>, types: Set<String>): List<Pal> {
        if (minSuitability.isEmpty() && types.isEmpty()) return emptyList()
        return palRepository.getPals().filter { pal ->
            val suitabilityOk = minSuitability.all { (work, minLevel) ->
                (pal.suitability[work]?.baseLevel ?: 0) >= minLevel
            }
            val typeOk = types.isEmpty() || types.all { it in pal.types }
            suitabilityOk && typeOk
        }
    }

    private fun getIndex() : List<SearchResult> {
        indexCache?.let { return it }

        val results = mutableListOf<SearchResult>()

        palRepository.getPals().forEach { pal ->
            results.add(
                SearchResult(
                    name = pal.palname,
                    iconPath = "file:///android_asset/${pal.iconAssetpath}",
                    target = SearchNavTarget.PalDetail(pal.stats.code)
                )
            )
        }

        ItemCategory.entries.forEach { category ->
            itemRepository.getItems(category).forEach { item ->
                results.add(
                    SearchResult(item.name, item.iconAssetPath(category), SearchNavTarget.Item(category, item.href))
                )
            }
        }

        BuildingCategory.entries.forEach { category ->
            buildingRepository.getBuildings(category).forEach { building ->
                results.add(
                    SearchResult(building.name, building.iconAssetPath(category), SearchNavTarget.Building(category, building.href))
                )
            }
        }

        indexCache = results
        return results
    }
}
