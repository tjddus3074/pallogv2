package com.mackereldev.pallogv2.data.breeding

import com.mackereldev.pallogv2.data.model.BreedingPal
import com.mackereldev.pallogv2.data.model.UniqueComboEntry
import kotlin.math.abs

class PalBreedingLogic(
    private val pals: Map<String, BreedingPal>,
    val uniqueCombos: List<UniqueComboEntry>
) {
    val palCodes: Set<String> get() = pals.keys

    // 특수 조합으로만 나오는 자식들은 일반 교배 풀에서 제외
    private val uniqueChildCodes: Set<String> = uniqueCombos.map { it.childCode }.toSet()

    private val allPalsByBreedPower = pals.values.sortedBy { it.breedingPower }
    private val idToAllBreedPowerIndex = allPalsByBreedPower.mapIndexed { index, pal -> pal.id to index }.toMap()

    private val breedablePalsByBreedPower = allPalsByBreedPower.filter { it.id !in uniqueChildCodes }
    private val idToBreedPowerIndex = breedablePalsByBreedPower.mapIndexed { index, pal -> pal.id to index }.toMap()

    // 부모쌍 -> 자식 목록 (성별에 따라 자식이 갈리는 조합이 있어 리스트)
    private val uniquePairs: Map<Long, List<UniqueComboEntry>> = uniqueCombos
        .mapNotNull { combo ->
            val p1 = pals[combo.parent1Code]
            val p2 = pals[combo.parent2Code]
            if(p1 == null || p2 == null) null else getPairId(p1, p2) to combo
        }
        .groupBy({ it.first }, { it.second })

    private fun isUnique(pal: BreedingPal): Boolean = pal.id in uniqueChildCodes

    private fun getPairId(parent1: BreedingPal, parent2: BreedingPal): Long =
        if (parent1.sortIndex < parent2.sortIndex) {
            (parent1.sortIndex.toLong() shl 10) + parent2.sortIndex
        } else {
            (parent2.sortIndex.toLong() shl 10) + parent1.sortIndex
        }

    private fun checkUniquePair(parent1: BreedingPal, parent2: BreedingPal): List<String>? =
        uniquePairs[getPairId(parent1, parent2)]?.map { it.childCode }

    // 선택한 두 부모가 특수 조합인지 (성별 조건 표시용)
    fun uniqueCombosForPair(code1: String, code2: String): List<UniqueComboEntry> {
        val p1 = pals[code1] ?: return emptyList()
        val p2 = pals[code2] ?: return emptyList()
        return uniquePairs[getPairId(p1, p2)].orEmpty()
    }

    fun uniqueCombosForChild(childCode: String) : List<UniqueComboEntry> =
        uniqueCombos.filter { it.childCode == childCode }

    private fun computeBabyPower(parent1: BreedingPal, parent2: BreedingPal): Int =
        (parent1.breedingPower + parent2.breedingPower + 1) / 2

    private fun comparePowerGap(babyPower: Int, option1: BreedingPal, option2: BreedingPal): Boolean {
        val diff1 = abs(babyPower - option1.breedingPower)
        val diff2 = abs(babyPower - option2.breedingPower)
        return diff1 < diff2 || (diff1 == diff2 && option1.tiebreakIndex < option2.tiebreakIndex)
    }

    private fun sortPairList(list: List<Pair<BreedingPal, BreedingPal>>): List<Pair<BreedingPal, BreedingPal>> =
        list.sortedWith { pair1, pair2 ->
            val (a1, a2) = pair1
            val (b1, b2) = pair2
            if(a1.id == b1.id) a2.sortIndex - b2.sortIndex else a1.sortIndex - b1.sortIndex
        }

    fun getChildren(parentId1: String?, parentId2: String?) : Map<String, List<Pair<BreedingPal, BreedingPal>>> {
        if(parentId1 == null && parentId2 == null) return emptyMap()

        if(parentId1 == null || parentId2 == null) {
            val parent = pals[parentId1 ?: parentId2] ?: return emptyMap()
            val results = mutableMapOf<String, MutableList<Pair<BreedingPal, BreedingPal>>>()
            var currentIndex = 0

            allPalsByBreedPower.forEach { otherParent ->
                val uniquePair = checkUniquePair(parent, otherParent)
                if(uniquePair != null) {
                    uniquePair.forEach { childId ->
                        results.getOrPut(childId) { mutableListOf() }.add(parent to otherParent)
                    }
                    return@forEach
                }

                val babyPower = computeBabyPower(parent, otherParent)
                while(currentIndex < breedablePalsByBreedPower.size - 1 &&
                    !comparePowerGap(babyPower, breedablePalsByBreedPower[currentIndex], breedablePalsByBreedPower[currentIndex+1])
                ) {
                    currentIndex++
                }

                results.getOrPut(breedablePalsByBreedPower[currentIndex].id) { mutableListOf() }.add(parent to otherParent)
            }
            return results.mapValues { sortPairList(it.value) }
        }

        val parent1 = pals[parentId1] ?: return emptyMap()
        val parent2 = pals[parentId2] ?: return emptyMap()

        if(parent1.sortIndex == parent2.sortIndex) return mapOf(parent1.id to listOf(parent1 to parent2))

        val uniquePair = checkUniquePair(parent1, parent2)
        if(uniquePair != null) return uniquePair.associateWith { listOf(parent2 to parent2) }

        val babyPower = computeBabyPower(parent1, parent2)

        var left = if(parent1.breedingPower < parent2.breedingPower) {
            if(isUnique(parent1)) 0 else idToBreedPowerIndex[parent1.id] ?: 0
        } else {
            if(isUnique(parent2)) 0 else idToBreedPowerIndex[parent2.id] ?: 0
        }

        var right = if(parent1.breedingPower < parent2.breedingPower) {
            if(isUnique(parent2)) breedablePalsByBreedPower.size - 1
            else idToBreedPowerIndex[parent2.id] ?: (breedablePalsByBreedPower.size - 1)
        } else {
            if(isUnique(parent1)) breedablePalsByBreedPower.size - 1
            else idToBreedPowerIndex[parent1.id] ?: (breedablePalsByBreedPower.size - 1)
        }

        while(right - left > 2) {
            val middle = (right + left) / 2
            if(breedablePalsByBreedPower[middle].breedingPower < babyPower) left = middle else right = middle
        }

        var best = left
        for(i in left + 1..right) {
            if(i < breedablePalsByBreedPower.size &&
                comparePowerGap(babyPower, breedablePalsByBreedPower[i], breedablePalsByBreedPower[best])
                ) {
                best = i
            }
        }

        return mapOf(breedablePalsByBreedPower[best].id to listOf(parent1 to parent2))
    }

    fun getParentPairs(childId: String?, sorted: Boolean = true): List<Pair<BreedingPal, BreedingPal>> {
        if(childId == null) return emptyList()
        val child = pals[childId] ?: return emptyList()

        if(isUnique(child)) {
            val pairs = uniqueCombosForChild(child.id).mapNotNull { combo ->
                val p1 = pals[combo.parent1Code]
                val p2 = pals[combo.parent2Code]
                if(p1 != null && p2 != null) p1 to p2 else null
            }
            return if(sorted) sortPairList(pairs) else pairs
        }

        val pairs = mutableListOf<Pair<BreedingPal, BreedingPal>>()
        val bpi = idToBreedPowerIndex[child.id] ?: return emptyList()
        val notFirst = bpi != 0
        val notLast = bpi != idToBreedPowerIndex.size - 1
        var si = idToAllBreedPowerIndex[child.id] ?: return emptyList()

        for(p1 in si downTo 0) {
            for(p2 in si until allPalsByBreedPower.size) {
                val parent1 = allPalsByBreedPower[p1]
                val parent2 = allPalsByBreedPower[p2]
                val babyPower = computeBabyPower(parent1, parent2)
                val selfDiff = abs(babyPower - child.breedingPower)

                if(notFirst) {
                    val otherPal = breedablePalsByBreedPower[bpi - 1]
                    val otherDiff = abs(babyPower - otherPal.breedingPower)
                    if(otherDiff < selfDiff || (otherDiff == selfDiff && otherPal.tiebreakIndex < child.tiebreakIndex)) {
                        si++
                        continue
                    }
                }

                if(notLast) {
                    val otherPal = breedablePalsByBreedPower[bpi + 1]
                    val otherDiff = abs(babyPower - otherPal.breedingPower)
                    if(otherDiff < selfDiff || (otherDiff == selfDiff && otherPal.tiebreakIndex < child.tiebreakIndex)) {
                        break
                    }
                }

                if(parent1.sortIndex < parent2.sortIndex) pairs.add(parent1 to parent2) else pairs.add(parent2 to parent1)
            }
        }

        return if(sorted) sortPairList(pairs) else pairs
    }


}