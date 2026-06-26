/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.entity.data

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import priv.seventeen.artist.arcartx.ArcartX
import priv.seventeen.artist.arcartx.core.config.area.Area
import priv.seventeen.artist.arcartx.core.effect.data.EffectPosition
import priv.seventeen.artist.arcartx.core.effect.data.WorldTextureBuilder
import priv.seventeen.artist.arcartx.event.player.PlayerExtraSlotUpdateEvent
import priv.seventeen.artist.arcartx.event.player.PlayerModelUpdateEvent
import priv.seventeen.artist.arcartx.network.NetworkMessageSender
import priv.seventeen.artist.arcartx.network.encryptor.AESEncryptor
import priv.seventeen.artist.arcartx.nms.AsteroidScheduler
import priv.seventeen.artist.arcartx.script.ScriptManager
import priv.seventeen.artist.arcartx.util.EntityUtils.doWithSeenBy
import priv.seventeen.artist.arcartx.util.collections.CallBack
import priv.seventeen.artist.blink.bukkitPlugin
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max

class ArcartXPlayer(val player: Player) : ArcartXEntity(player){

    var encryptor : AESEncryptor? = null

    var key : String? = null

    var alt: Boolean = false

    val callbacks: MutableMap<String, CallBack> = HashMap()

    val crc64 : MutableSet<Long> = HashSet()

    var area : Area? = null

    private val slotCache : MutableMap<String, ItemStack> = ConcurrentHashMap()

    private val tagCooldown : MutableMap<String, Long> = HashMap()

    private var tryModelTask : Any? = null

    private var controller : String? = null

    var playerModel: String = ""
        private set

    var playerModelScale = 1.0
        private set


    // 额外模型
    private var extraModels = HashMap<String, String>()


    private var substitutionMode = SubstitutionMode.TRADITION_HIDE.id

    // TYPE - MODEL_ID
    private var substitutionModels = HashMap<String, String>()

    private var substitutionModelID = ""

    // 标记是否发送资源
    var sentResource = false


    var jumpTime = 0L


    init {
        ArcartX.databaseManager?.let { database ->
            val keys = ArrayList(ArcartX.configs.slotFolder.setting.keys)
            // 异步执行 JDBC 查询，避免在玩家对象构造（可能落在主线程）时阻塞；
            // NMS 物品反序列化须回到主线程
            AsteroidScheduler.runAsync(bukkitPlugin, Runnable {
                val raw = database.slotDatabase.loadPlayerSlotDataRaw(player.uniqueId, keys)
                AsteroidScheduler.ensureMainThread(bukkitPlugin, Runnable {
                    raw.forEach { (slot, json) ->
                        database.slotDatabase.deserializeItemStack(json)?.let { slotCache[slot] = it }
                    }
                })
            })
        }
    }

    override fun setModel(modelID: String, scale: Double) {
        super.setModel(modelID, scale)
        playerModel = modelID
        playerModelScale = scale
        PlayerModelUpdateEvent(player, modelID).call()
    }


    override fun removeModel() {
        super.removeModel()
        playerModel = ""
        playerModelScale = 1.0
    }

    fun addExtraModel(locator: String, modelID: String ) {
        extraModels[locator] = modelID
        player.doWithSeenBy { NetworkMessageSender.sendAddExtraModel(it, player.uniqueId, locator, modelID) }
    }

    fun removeExtraModel(locator: String) {
        extraModels.remove(locator)
        player.doWithSeenBy { NetworkMessageSender.sendRemoveExtraModel(it, player.uniqueId, locator) }
    }

    fun clearExtraModels() {
        extraModels.clear()
        player.doWithSeenBy { NetworkMessageSender.sendClearExtraModel(it, player.uniqueId) }
    }

    @Deprecated(
        "弃用，改用基于 SubstitutionMode 的重载",
        replaceWith = ReplaceWith(
            "setSubstitutionModel(if (mode) SubstitutionMode.TRADITION_HIDE else SubstitutionMode.FULL, modelID)",
            "priv.seventeen.artist.arcartx.core.entity.data.SubstitutionMode"
        )
    )
    fun setSubstitutionModel(modelID: String, mode: Boolean) {
        substitutionModels.clear()
        substitutionMode = if (mode)  SubstitutionMode.TRADITION_HIDE.id else SubstitutionMode.FULL.id
        substitutionModelID = modelID
        player.doWithSeenBy { NetworkMessageSender.sendSubstitutionModel(it, player.uniqueId, substitutionModelID, substitutionModels, substitutionMode) }
    }

    fun setSubstitutionModel(mode: SubstitutionMode, modelID: String = "", models: Map<SingleModelType, String> = mapOf()) {
        substitutionModels.clear()
        substitutionModelID = ""
        substitutionMode = mode.id

        if(mode == SubstitutionMode.TRADITION_SINGLE){
            models.forEach { (type, modelID) ->
                substitutionModels[type.id] = modelID
            }
        } else {
            substitutionModelID = modelID
        }

        player.doWithSeenBy { NetworkMessageSender.sendSubstitutionModel(it, player.uniqueId, substitutionModelID, substitutionModels, substitutionMode) }
    }

    fun removeSubstitutionModel() {
        substitutionModels.clear()
        substitutionModelID = ""
        substitutionMode = SubstitutionMode.NONE.id
        player.doWithSeenBy { NetworkMessageSender.sendSubstitutionModel(it, player.uniqueId, substitutionModelID, substitutionModels, substitutionMode) }
    }

    fun playFirstPersonAnimationByTime(animation: String, speed: Double, keepTime: Int){
        NetworkMessageSender.sendPlayerFirstPersonAnimationByTime(player, animation, keepTime, speed)
    }

    fun playFirstPersonAnimationByCountOf(animation: String, speed: Double, count: Int){
        NetworkMessageSender.sendPlayerFirstPersonAnimationByCount(player, animation, count, speed)
    }


    override fun startSeenBy(target: Player) {
        super.startSeenBy(target)
        NetworkMessageSender.sendSetController(target,player.uniqueId , controller?:"")
        extraModels.forEach { (key, value) ->
            NetworkMessageSender.sendAddExtraModel(target, player.uniqueId, key, value)
        }
        NetworkMessageSender.sendSubstitutionModel(target, player.uniqueId, substitutionModelID, substitutionModels, substitutionMode)
    }

    fun setController(controller: String) {
        this.controller = controller
        player.doWithSeenBy { NetworkMessageSender.sendSetController(it, player.uniqueId, controller) }
    }


    fun tryModel(modelID: String, scale: Double, time: Long){
        super.setModel(modelID, scale)
        AsteroidScheduler.cancelTask(tryModelTask)
        tryModelTask = AsteroidScheduler.runTaskLater(bukkitPlugin, Runnable {
            super.setModel(playerModel, playerModelScale)
        }, time / 50)
    }


    fun setSlotItemStackOnlyClient(slotID : String, itemStack: ItemStack){
        NetworkMessageSender.sendSlotItemStack(player, slotID, itemStack)
    }

    fun removeSlotItemStackOnlyClient(slotID: String, startWith: Boolean){
        NetworkMessageSender.sendSlotItemRemove(player, slotID, startWith)
    }

    fun setSlotItemStack(slotID: String, itemStack: ItemStack){
        slotCache[slotID] = itemStack
        // 主线程序列化（NMS 安全）后异步落库，避免主线程阻塞在 DB 往返上
        ArcartX.databaseManager?.slotDatabase?.let { db ->
            val json = db.serializeItemStack(itemStack)
            AsteroidScheduler.runAsync(bukkitPlugin, Runnable {
                db.setSlotDataJson(player.uniqueId, slotID, json)
            })
        }
        setSlotItemStackOnlyClient(slotID, itemStack)
        PlayerExtraSlotUpdateEvent(player,slotID,itemStack).call()
        ArcartX.configs.slotFolder.setting[slotID]?.updateScriptArgs?.let {
            it.forEach{ arg ->
                ScriptManager.executeScript(arg.first, player, itemStack,arg.second)
            }
        }
    }

    fun syncSlotCacheToClient(){
        slotCache.forEach { (slotID, itemStack) ->
            setSlotItemStackOnlyClient(slotID, itemStack)
            PlayerExtraSlotUpdateEvent(player,slotID,itemStack).call()
            ArcartX.configs.slotFolder.setting[slotID]?.updateScriptArgs?.let {
                it.forEach{ arg ->
                    ScriptManager.executeScript(arg.first, player, itemStack,arg.second)
                }
            }
        }
    }

    fun getSlotItemStack(slotID: String) : ItemStack? {
        return slotCache[slotID]
    }

    fun getTagCooldown(tag : String) : Long {
        if(tagCooldown.containsKey(tag)) {
            return max(tagCooldown[tag]!! - System.currentTimeMillis(), 0)
        }
        return 0
    }

    fun setTagCooldown(tag : String, time : Long) {
        tagCooldown[tag] = time + System.currentTimeMillis()
        NetworkMessageSender.sendItemCoolDown(player, tag, time)
    }


    fun setState(controller: String, state: String){
        player.doWithSeenBy { NetworkMessageSender.sendState(it,player.uniqueId , controller, state, 1.0, -1) }
    }

    fun setState(controller: String, state: String, speed: Double){
        player.doWithSeenBy { NetworkMessageSender.sendState(it,player.uniqueId , controller, state,speed, -1) }
    }

    fun setState(controller: String, state: String, speed: Double, moveBreak: Long){
        player.doWithSeenBy { NetworkMessageSender.sendState(it,player.uniqueId , controller, state,speed, moveBreak) }
    }

    fun addWayPoint(id: String, title: String, waypointConfigId: String, x: Double, y: Double, z: Double) {
        NetworkMessageSender.sendWaypointCreate(player, id, title, waypointConfigId, x, y, z)
    }

    fun deleteWayPoint(id: String, regex: Boolean = false) {
        NetworkMessageSender.sendWaypointRemove(player, id,regex)
    }

    fun clearWayPoint() {
        NetworkMessageSender.sendWaypointRemoveAll(player)
    }

    fun addDamageDisplay(damageDisplayConfigId: String, x: Double, y: Double, z: Double, damage: Double) {
        NetworkMessageSender.sendDamageDisplay(player, damageDisplayConfigId, x, y, z, damage)
    }

    fun addDamageDisplay(damageDisplayConfigId: String, target: Entity, damage: Double) {
        val loc = target.location
        val x = loc.x
        val y = loc.y + target.height / 2
        val z = loc.z
        NetworkMessageSender.sendDamageDisplay(player, damageDisplayConfigId, x, y, z, damage)
    }




    fun playSoundForSelf(resourcePath: String, soundCategory: String, pitch: Float, keepTime: Int) {
        NetworkMessageSender.sendPlayEntitySound(
            player, resourcePath, player.uniqueId.toString(),
            soundCategory, 16,
            pitch.toDouble(), keepTime
        )
    }

    fun sendCustomPacket(id: String, vararg data: String) {
        NetworkMessageSender.sendCustomPacket(player, id, *data)
    }

    fun setClientTitle(text: String) {
        NetworkMessageSender.sendClientTitle(player, text)
    }

    fun parseShimmer(code: String) {
        NetworkMessageSender.sendExecuteScript(player, code, true)
    }

    fun setThirdPerson(enable: Boolean) {
        NetworkMessageSender.setThirdPerson(player, enable)
    }

    fun setViewLock(enable: Boolean) {
        NetworkMessageSender.setViewLock(player, if (enable) 2 else 0)
    }

    fun setViewLockMode(mode: Int) {
        NetworkMessageSender.setViewLock(player, mode)
    }

    fun setCameraFromPreset(id: String) {
        NetworkMessageSender.setCameraFromPreset(player, id)
    }

    fun setCameraLocation(offsetX: Double, offsetY: Double, offsetZ: Double, freeView: Boolean) {
        NetworkMessageSender.setCamera(player, offsetX, offsetY, offsetZ, freeView)
    }

    fun startSceneCamera(sceneId: String) {
        NetworkMessageSender.sendSceneCamera(player, sceneId)
    }

    fun stopSceneCamera(){
        NetworkMessageSender.sendSceneCameraStop(player)
    }

    fun enableShader(shader: String) {
        NetworkMessageSender.sendStartShader(player, shader)
    }

    fun disableShader() {
        NetworkMessageSender.sendCloseShader(player)
    }

    fun setSkyTexture(texturePath: String, forceNoCloud: Boolean) {
        NetworkMessageSender.sendSetSkyBoxTexture(player, texturePath, forceNoCloud)
    }

    fun clearSkyTexture() {
        NetworkMessageSender.sendClearSkyBoxTexture(player)
    }

    @Deprecated("弃用", replaceWith = ReplaceWith("spawnBedrockParticle(identifier, particleID, effectPosition)"))
    fun spawnBedrockParticle(
        id: String,
        x: Double,
        y: Double,
        z: Double,
        yaw: Float,
        pitch: Float,
    ) {
        NetworkMessageSender.sendBedrockParticle(player, id, EffectPosition.location(x,y,z, pitch, yaw))
    }

    fun spawnBedrockParticle(
        identifier: String, particleID: String, effectPosition: EffectPosition
    ) {
        NetworkMessageSender.sendBedrockParticle(player, particleID, effectPosition)
    }




    fun playBlockAnimation(
        x: Int,
        y: Int,
        z: Int,
        animation: String,
        speed: Double,
        transitionTime: Int,
        keepTime: Long
    ) {
        NetworkMessageSender.sendBlockAnimation(player, x, y, z, animation, speed, transitionTime, keepTime)
    }

    fun spawnHammerCrackEffect(
        x: Int,
        y: Int,
        z: Int,
        radius: Float,
        depth: Float,
        `in`: Int,
        keep: Int,
        out: Int,
        mode: Int
    ) {
        NetworkMessageSender.sendHammerCrack(player, x, y, z, radius, depth, `in`, keep, out, mode)
    }

    fun sendChatCard(cardID: String, cardData: Map<String, String>) {
        NetworkMessageSender.sendCardMessage(player, cardID, cardData)
    }

    fun addWorldTexture(id: String, builder: WorldTextureBuilder, effectPosition: EffectPosition){
        NetworkMessageSender.sendWorldTexture(player, id, builder, effectPosition)
    }

    fun removeWorldTexture(id: String) {
        NetworkMessageSender.sendWorldTextureRemove(player, id)
    }





    enum class SubstitutionMode(val id: String) {
        FULL("FULL"), // 该模式只允许穿戴一个
        TRADITION("TRADITION"), // 该模式只允许穿戴一个
        TRADITION_HIDE("TRADITION_HIDE"), // 该模式只允许穿戴一个
        TRADITION_SINGLE("TRADITION_SINGLE"), // 该模式允许穿戴多个
        NONE("NONE")
    }

    enum class SingleModelType(val id: String) {

        HEAD("HEAD"){
            override fun incompatible(): SingleModelType {
                return HEAD_HIDE
            }
        },
        UPPER_BODY("UPPER_BODY"){
            override fun incompatible(): SingleModelType {
                return UPPER_BODY_HIDE
            }
        },
        LOWER_BODY("LOWER_BODY"){
            override fun incompatible(): SingleModelType {
                return LOWER_BODY_HIDE
            }
        },
        FOOT("FOOT"){
            override fun incompatible(): SingleModelType {
                return FOOT_HIDE
            }
        },

        HEAD_HIDE("HEAD_HIDE"){
            override fun incompatible(): SingleModelType {
                return HEAD
            }
        },
        UPPER_BODY_HIDE("UPPER_BODY_HIDE"){
            override fun incompatible(): SingleModelType {
                return UPPER_BODY
            }
        },
        LOWER_BODY_HIDE("LOWER_BODY_HIDE"){
            override fun incompatible(): SingleModelType {
                return LOWER_BODY
            }
        },
        FOOT_HIDE("FOOT_HIDE"){
            override fun incompatible(): SingleModelType {
                return FOOT
            }
        },

        OTHER("OTHER");

        // 不兼容

        open fun incompatible(): SingleModelType{
            return OTHER
        }

    }


}