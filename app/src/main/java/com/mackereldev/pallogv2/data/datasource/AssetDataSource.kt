package com.mackereldev.pallogv2.data.datasource

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mackereldev.pallogv2.data.model.AccessoryItem
import com.mackereldev.pallogv2.data.model.AmmoItem
import com.mackereldev.pallogv2.data.model.ArmorItem
import com.mackereldev.pallogv2.data.model.ArmorLevel
import com.mackereldev.pallogv2.data.model.BreedingPal
import com.mackereldev.pallogv2.data.model.BreedingPalData
import com.mackereldev.pallogv2.data.model.Building
import com.mackereldev.pallogv2.data.model.Item
import com.mackereldev.pallogv2.data.model.Pal
import com.mackereldev.pallogv2.data.model.SphereItem
import com.mackereldev.pallogv2.data.model.SphereModuleItem
import com.mackereldev.pallogv2.data.model.TechItem
import com.mackereldev.pallogv2.data.model.TechRow
import com.mackereldev.pallogv2.data.model.WeaponItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun loadPals() : List<Pal> {
        val json = context.assets.open("PAL/Json/pals_data.json")
            .bufferedReader()
            .use { it.readText() }
        return Gson().fromJson(json, object : TypeToken<List<Pal>>() {}.type)
    }

    fun loadItems(fileName: String): List<Item> {
        val json = context.assets.open("PAL/Json/$fileName")
            .bufferedReader()
            .use { it.readText() }
        return Gson().fromJson(json, object : TypeToken<List<Item>>() {}.type)
    }

    fun loadBuildings(fileName: String): List<Building> {
        val json = context.assets.open("PAL/Json/$fileName")
            .bufferedReader()
            .use { it.readText() }
        return Gson().fromJson(json, object : TypeToken<List<Building>>() {}.type)
    }

    fun loadWeapon(fileName: String): List<WeaponItem> {
        val json = context.assets.open("PAL/Json/$fileName")
            .bufferedReader()
            .use { it.readText() }
        return Gson().fromJson(json, object : TypeToken<List<WeaponItem>>() {}.type)
    }

    fun loadAmmo(fileName: String): List<AmmoItem> {
        val json = context.assets.open("PAL/Json/$fileName")
            .bufferedReader()
            .use { it.readText() }
        return Gson().fromJson(json, object : TypeToken<List<AmmoItem>>() {}.type)
    }

    fun loadArmor(fileName: String): List<ArmorItem> {
        val json = context.assets.open("PAL/Json/$fileName")
            .bufferedReader()
            .use { it.readText() }
        return Gson().fromJson(json, object : TypeToken<List<ArmorItem>>() {}.type)
    }

    fun loadSphere(fileName: String): List<SphereItem> {
        val json = context.assets.open("PAL/Json/$fileName")
            .bufferedReader()
            .use { it.readText() }
        return Gson().fromJson(json, object : TypeToken<List<SphereItem>>() {}.type)
    }

    fun loadSphereModule(fileName: String): List<SphereModuleItem> {
        val json = context.assets.open("PAL/Json/$fileName")
            .bufferedReader()
            .use { it.readText() }
        return Gson().fromJson(json, object : TypeToken<List<SphereModuleItem>>() {}.type)
    }

    fun loadAccessory(fileName: String): List<AccessoryItem> {
        val json = context.assets.open("PAL/Json/$fileName")
            .bufferedReader()
            .use { it.readText() }
        return Gson().fromJson(json, object : TypeToken<List<AccessoryItem>>() {}.type)
    }

    fun loadBreedingPals(): Map<String, BreedingPal> {
        val json = context.assets.open("PAL/Json/pal_breeding_new.json")
            .bufferedReader()
            .use { it.readText() }
        return Gson().fromJson(json, BreedingPalData::class.java).pals
    }

    fun loadTechRows(): List<TechRow> {
        val json = context.assets.open("PAL/Json/Tech.json")
            .bufferedReader()
            .use { it.readText() }
        val type = object : TypeToken<List<Map<String, List<TechItem>>>>() {}.type
        val raw: List<Map<String, List<TechItem>>> = Gson().fromJson(json, type)
        return raw.mapNotNull { row ->
            val entry = row.entries.firstOrNull() ?: return@mapNotNull null
            TechRow(level = entry.key, items = entry.value)
        }
    }
}