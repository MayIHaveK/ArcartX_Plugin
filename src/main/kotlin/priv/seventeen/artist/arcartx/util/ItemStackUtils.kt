/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.util

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.arcartx.nms.ItemBridge.editTag
import priv.seventeen.artist.arcartx.nms.ItemBridge.getDeepTag
import priv.seventeen.artist.arcartx.nms.ItemBridge.getItemTag
import priv.seventeen.artist.arcartx.util.ChatUtils.replaceColorChar
import priv.seventeen.artist.asteroid.item.ItemTag
import priv.seventeen.artist.asteroid.item.ItemTagData
import priv.seventeen.artist.asteroid.item.ItemTagType

/** 物品工具类 */
object ItemStackUtils {

    @JvmStatic
    fun ItemStack.addLore(text: String){
        val meta: ItemMeta? = this.itemMeta
        checkNotNull(meta)
        val lore = if (meta.hasLore()) meta.lore else ArrayList()
        checkNotNull(lore)
        lore.add(text.replaceColorChar())
        meta.lore = lore
        this.setItemMeta(meta)
    }

    @JvmStatic
    fun ItemStack.setLore(text: String, line: Int){
        val meta: ItemMeta? = this.itemMeta
        checkNotNull(meta)
        val lore = if (meta.hasLore()) meta.lore else ArrayList()
        checkNotNull(lore)
        lore[line] = text.replaceColorChar()
        meta.lore = lore
        this.setItemMeta(meta)
    }

    @JvmStatic
    fun ItemStack.insertLore(text: String, line: Int){
        val meta: ItemMeta? = this.itemMeta
        checkNotNull(meta)
        val lore = if (meta.hasLore()) meta.lore else ArrayList()
        checkNotNull(lore)
        lore.add(line, text.replaceColorChar())
        meta.lore = lore
        this.setItemMeta(meta)
    }

    @JvmStatic
    fun ItemStack.removeLore(line: Int){
        val meta: ItemMeta? = this.itemMeta
        checkNotNull(meta)
        val lore = if (meta.hasLore()) meta.lore else ArrayList()
        checkNotNull(lore)
        lore.removeAt(line)
        meta.lore = lore
        this.setItemMeta(meta)
    }

    @JvmStatic
    fun ItemStack.setDisplayName(name: String){
        val meta: ItemMeta? = this.itemMeta
        checkNotNull(meta)
        meta.setDisplayName(name.replaceColorChar())
        this.setItemMeta(meta)
    }

    @JvmStatic
    fun ItemStack.setIcon(path: String) {
        this.itemMeta = this.editTag { this["icon"] = ItemTagData.of(path) }.itemMeta
    }

    @JvmStatic
    fun ItemStack.setDrop(path: String) {
        this.itemMeta = this.editTag { this["drop"] = ItemTagData.of(path) }.itemMeta
    }

    @JvmStatic
    fun ItemStack.setModel(path: String){
        this.itemMeta = this.editTag { this["model"] = ItemTagData.of(path) }.itemMeta
    }

    @JvmStatic
    fun ItemStack.setFPModel(path: String){
        this.itemMeta = this.editTag { this["fp_model"] = ItemTagData.of(path) }.itemMeta
    }

    @JvmStatic
    fun ItemStack.addExtraModel(locatorName: String, modelID: String){
        this.itemMeta = this.editTag {
            val current = this["extra_model"]
            if(current != null && current.type == ItemTagType.COMPOUND){
                current.asCompound().put(locatorName, ItemTagData.of(modelID))
            } else {
                this.put("extra_model", ItemTagData.of(ItemTag().also { it[locatorName] = ItemTagData.of(modelID) }))
            }
        }.itemMeta
    }

    @JvmStatic
    fun ItemStack.setUrl(path: String){
        this.itemMeta = this.editTag { this["url"] = ItemTagData.of(path) }.itemMeta
    }

    @JvmStatic
    fun ItemStack.setCoolDownTag(value: String){
        this.itemMeta = this.editTag { this["cooldown"] = ItemTagData.of(value) }.itemMeta
    }

    @JvmStatic
    fun ItemStack.getCooldownTag(): String {
        return this.getItemTag()["cooldown"]?.asString() ?: ""
    }

    @JvmStatic
    fun ItemStack.getCooldown(player: Player) : Long{
        val tag = this.getItemTag()["cooldown"]?.asString() ?: return 0
        return ArcartXEntityManager.getPlayer(player)?.getTagCooldown(tag) ?: 0
    }

    @JvmStatic
    fun ItemStack.setCooldown(player: Player, time: Long){
        val tag = this.getItemTag()["cooldown"]?.asString() ?: return
        ArcartXEntityManager.getPlayer(player)?.setTagCooldown(tag, time)
    }

    @JvmStatic
    fun ItemStack.containsLore(text: String): Boolean {
        val meta = this.itemMeta?: return false
        val lore = meta.lore?: return false
        lore.forEach{
            if(it.contains(text)){
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun ItemStack.hasLore(text: String): Boolean {
        val meta = this.itemMeta?: return false
        val lore = meta.lore?: return false
        return lore.contains(text)
    }

    @JvmStatic
    fun ItemStack.getTag(key: String): String {
        return this.getDeepTag(key)?.asString() ?: ""
    }

}
