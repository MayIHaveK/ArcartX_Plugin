/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.effect.data

import com.google.gson.annotations.SerializedName

class WorldTextureBuilder {

    // 贴图
    @SerializedName("texture")
    private var texture: String = ""

    @SerializedName("width")
    private var width: Double = 100.0

    @SerializedName("height")
    private var height: Double = 100.0

    // 朝向玩家视角
    @SerializedName("facing")
    private var facing: Boolean = false

    // 发光
    @SerializedName("glowing")
    private var glowing: Boolean = false

    // 生命周期
    @SerializedName("lifeTime")
    private var lifeTime: Int = 1000

    // 动画
    @SerializedName("animation")
    private var animation = ArrayList<WorldTextureAnimation>()


    fun setTexture(texture: String, facing: Boolean, glowing: Boolean): WorldTextureBuilder {
        this.texture = texture
        this.facing = facing
        this.glowing = glowing
        return this
    }

    fun setSize(width: Double, height: Double): WorldTextureBuilder {
        this.width = width
        this.height = height
        return this
    }

    fun setLifeTime(lifeTime: Int): WorldTextureBuilder {
        this.lifeTime = lifeTime
        return this
    }

    fun addOffsetXAnimation(delay: Int, from: Double, to: Double, duration: Int, loop: Boolean): WorldTextureBuilder =
        addAnimation(AnimationType.OFFSET_X, delay, from, to, duration, loop)

    fun addOffsetYAnimation(delay: Int, from: Double, to: Double, duration: Int, loop: Boolean): WorldTextureBuilder =
        addAnimation(AnimationType.OFFSET_Y, delay, from, to, duration, loop)

    fun addOffsetZAnimation(delay: Int, from: Double, to: Double, duration: Int, loop: Boolean): WorldTextureBuilder =
        addAnimation(AnimationType.OFFSET_Z, delay, from, to, duration, loop)

    fun addScaleAnimation(delay: Int, from: Double, to: Double, duration: Int, loop: Boolean): WorldTextureBuilder =
        addAnimation(AnimationType.SCALE, delay, from, to, duration, loop)

    fun addRotationXAnimation(delay: Int, from: Double, to: Double, duration: Int, loop: Boolean): WorldTextureBuilder =
        addAnimation(AnimationType.ROTATION_X, delay, from, to, duration, loop)

    fun addRotationYAnimation(delay: Int, from: Double, to: Double, duration: Int, loop: Boolean): WorldTextureBuilder =
        addAnimation(AnimationType.ROTATION_Y, delay, from, to, duration, loop)

    fun addRotationZAnimation(delay: Int, from: Double, to: Double, duration: Int, loop: Boolean): WorldTextureBuilder =
        addAnimation(AnimationType.ROTATION_Z, delay, from, to, duration, loop)

    /** 动画类型；key 为下发给客户端的序列化值，须与原字符串完全一致（不可改动，属协议） */
    private enum class AnimationType(val key: String) {
        OFFSET_X("x"), OFFSET_Y("y"), OFFSET_Z("z"),
        SCALE("scale"),
        ROTATION_X("rotationX"), ROTATION_Y("rotationY"), ROTATION_Z("rotationZ")
    }

    private fun addAnimation(type: AnimationType, delay: Int, from: Double, to: Double, duration: Int, loop: Boolean): WorldTextureBuilder {
        this.animation.add(WorldTextureAnimation(type.key, delay, from, to, duration, loop))
        return this
    }




}