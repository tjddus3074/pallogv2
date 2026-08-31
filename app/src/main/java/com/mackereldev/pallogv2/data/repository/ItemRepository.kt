package com.mackereldev.pallogv2.data.repository

import com.mackereldev.pallogv2.data.datasource.AssetDataSource
import com.mackereldev.pallogv2.data.model.AccessoryItem
import com.mackereldev.pallogv2.data.model.AmmoItem
import com.mackereldev.pallogv2.data.model.ArmorItem
import com.mackereldev.pallogv2.data.model.Item
import com.mackereldev.pallogv2.data.model.ItemCategory
import com.mackereldev.pallogv2.data.model.SphereItem
import com.mackereldev.pallogv2.data.model.SphereModuleItem
import com.mackereldev.pallogv2.data.model.WeaponItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepository @Inject constructor(
    private val dataSource: AssetDataSource
) {
    private val cache = mutableMapOf<ItemCategory, List<Item>>()
    private var weaponCache: List<WeaponItem>? = null
    private var ammoCache: List<AmmoItem>? = null
    private var armorCache: List<ArmorItem>? = null
    private var sphereCache: List<SphereItem>? = null
    private var sphereModuleCache: List<SphereModuleItem>? = null
    private var accessoryCache: List<AccessoryItem>? = null

    fun getItems(category: ItemCategory): List<Item> {
        return cache.getOrPut(category) {
            dataSource.loadItems(category.fileName)
        }
    }

    fun getWeapons(): List<WeaponItem> {
        if(weaponCache == null) weaponCache = dataSource.loadWeapon(ItemCategory.WEAPON.fileName)
        return weaponCache!!
    }

    fun getWeapon(href: String): WeaponItem? = getWeapons().find { it.href == href }
    fun getAmmoByCode(code: String): AmmoItem? = getAmmo().find { it.code == code }

    fun getAmmo(): List<AmmoItem> {
        if(ammoCache == null) ammoCache = dataSource.loadAmmo(ItemCategory.AMMO.fileName)
        return ammoCache!!
    }

    fun getAmmoItem(href: String): AmmoItem? = getAmmo().find { it.href == href }

    fun getArmor(): List<ArmorItem> {
        if(armorCache == null) armorCache = dataSource.loadArmor(ItemCategory.ARMOR.fileName)
        return armorCache!!
    }

    fun getArmorItem(href: String): ArmorItem? = getArmor().find { it.href == href }

    fun getSphere(): List<SphereItem> {
        if(sphereCache == null) sphereCache = dataSource.loadSphere(ItemCategory.SPHERE.fileName)
        return sphereCache!!
    }

    fun getSphereItem(href: String): SphereItem? = getSphere().find { it.href == href }

    fun getSphereModule(): List<SphereModuleItem> {
        if(sphereModuleCache == null) sphereModuleCache = dataSource.loadSphereModule(ItemCategory.SPHERE_MODULE.fileName)
        return sphereModuleCache!!
    }

    fun getSphereModuleItem(href: String): SphereModuleItem? = getSphereModule().find { it.href == href }

    fun getAccessories(): List<AccessoryItem> {
        if (accessoryCache == null) accessoryCache = dataSource.loadAccessory(ItemCategory.ACCESSORY.fileName)
        return accessoryCache!!
    }

    fun getAccessoryItem(href: String): AccessoryItem? = getAccessories().find { it.href == href }

    fun getMaterialIconPath(name: String): String? {
        getItems(ItemCategory.MATERIAL).find { it.name == name }?.let {
            return it.iconAssetPath(ItemCategory.MATERIAL)
        }
        getItems(ItemCategory.INGREDIENT).find { it.name == name }?.let {
            return it.iconAssetPath(ItemCategory.INGREDIENT)
        }
        return null
    }

    fun getMaterialItem(href: String): Item? = getItems(ItemCategory.MATERIAL).find { it.href == href }

    fun getConsumableItem(href: String): Item? = getItems(ItemCategory.CONSUMABLE).find { it.href == href }

    fun getIngredientItem(href: String): Item? = getItems(ItemCategory.INGREDIENT).find { it.href == href }

    fun getKeyItem(href: String): Item? = getItems(ItemCategory.KEY_ITEM).find { it.href == href }
}