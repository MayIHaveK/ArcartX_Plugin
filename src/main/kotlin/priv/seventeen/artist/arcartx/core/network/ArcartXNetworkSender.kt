/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.network

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.network.NetworkMessageSender
import java.util.*

/** 网络发送器公开接口 */
object ArcartXNetworkSender {

    fun sendCustomPacket(player: Player, id: String, vararg args: String){
        NetworkMessageSender.sendCustomPacket(player,id,*args)
    }

    fun sendServerVariable(player: Player, variableName: String, value: Any){
        NetworkMessageSender.sendServerVariable(player, variableName, value)
    }

    fun sendMultipleServerVariable(player: Player, variables: Map<String, Any>){
        variables.forEach{
            NetworkMessageSender.sendServerVariable(player, it.key, it.value)
        }
    }

    fun removeServerVariable(player: Player, variableName: String, startWith: Boolean = false){
        NetworkMessageSender.sendRemoveServerVariable(player, variableName, startWith)
    }

    fun sendDamageDisplay(player: Player,configID: String, damage: Double, location: Location){
        NetworkMessageSender.sendDamageDisplay(player, configID,location.x,location.y,location.z,damage)
    }

    fun sendDamageDisplay(player: Player,configID: String, damage: Double, target: Entity){
        NetworkMessageSender.sendDamageDisplay(player, configID,target.location.x,target.location.y + target.height / 2,target.location.z,damage)
    }

    fun sendBase64Image(player: Player, resourceID: String, base64: String){
        NetworkMessageSender.sendBase64Image(player, resourceID, base64)
    }

    fun sendSoundPlay(player: Player, name: String, path: String, x: Int, y: Int, z: Int, soundCategory: String = "master", distOrRoll: Int = 16, pitch: Double = 1.0, keepTime: Int = 5000) {
        NetworkMessageSender.sendPlayNameSound(player,path,x,y,z,soundCategory,distOrRoll,pitch,keepTime,name)
    }

    fun sendSoundPlay(player: Player, name: String, path: String, entity: Entity, soundCategory: String = "master", distOrRoll: Int = 16, pitch: Double = 1.0, keepTime: Int = 5000) {
        NetworkMessageSender.sendPlayEntitySound(player,path,entity.uniqueId.toString(),soundCategory,distOrRoll,pitch,keepTime,name)
    }

    fun sendSoundPlay(player: Player, path: String, entity: Entity, soundCategory: String = "master", distOrRoll: Int = 16, pitch: Double = 1.0, keepTime: Int = 5000) {
        NetworkMessageSender.sendPlayEntitySound(player,path,entity.uniqueId.toString(),soundCategory,distOrRoll,pitch,keepTime,null)
    }

    fun sendSoundPlay(player: Player, name: String, path: String, soundCategory: String = "master", distOrRoll: Int = 16, pitch: Double = 1.0, keepTime: Int = 5000) {
        NetworkMessageSender.sendPlayEntitySound(player,path,"self",soundCategory,distOrRoll,pitch,keepTime,name)
    }

    fun sendSoundPlay(player: Player, path: String, soundCategory: String = "master", distOrRoll: Int = 16, pitch: Double = 1.0, keepTime: Int = 5000) {
        NetworkMessageSender.sendPlayEntitySound(player,path,"self",soundCategory,distOrRoll,pitch,keepTime,null)
    }

    fun sendStopSound(player: Player, name: String){
        NetworkMessageSender.sendStopSound(player, name)
    }

    fun sendSetEntityModel(player: Player, entity: UUID, modelID: String, scale: Double = 1.0) {
        NetworkMessageSender.setEntityModel(player, entity, modelID, scale)
    }

    fun sendSetEntityAnimation(player: Player, entity: UUID, animationName: String, speed: Double, transitionTime: Int, time: Long) {
        NetworkMessageSender.sendEntityAnimation(player, entity, animationName, speed, transitionTime, time)
    }

    fun sendSetEntitySize(player: Player, entity: UUID, width: Double, height: Double){
        NetworkMessageSender.sendEntitySize(player, entity, width, height)
    }



}