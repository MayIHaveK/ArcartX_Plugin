/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.network.packet.server

import com.google.gson.annotations.SerializedName

class SPackPlaySound : ServerPacket {

    @SerializedName("path")
    private var path: String? = null

    @SerializedName("x")
    private var x: Int = 0

    @SerializedName("y")
    private var y: Int = 0

    @SerializedName("z")
    private var z: Int = 0

    @SerializedName("soundCategory")
    private var soundCategory: String? = null

    @SerializedName("distOrRoll")
    private var distOrRoll: Int = 0

    @SerializedName("pitch")
    private var pitch: Double = 0.0

    @SerializedName("keepTime")
    private var keepTime: Int = 0

    @SerializedName("key")
    private var key: String = ""

    @SerializedName("stop")
    private var stop: Boolean = false

    constructor(key: String, stop: Boolean) {
        this.key = key
        this.stop = stop
    }

    constructor(
        key: String,
        path: String,
        x: Int,
        y: Int,
        z: Int,
        soundCategory: String,
        distOrRoll: Int,
        pitch: Double,
        keepTime: Int
    ) {
        this.key = key
        this.path = path
        this.x = x
        this.y = y
        this.z = z
        this.soundCategory = soundCategory
        this.distOrRoll = distOrRoll
        this.pitch = pitch
        this.keepTime = keepTime
    }


    constructor(
        path: String,
        x: Int,
        y: Int,
        z: Int,
        soundCategory: String,
        distOrRoll: Int,
        pitch: Double,
        keepTime: Int
    ) {
        this.path = path
        this.x = x
        this.y = y
        this.z = z
        this.soundCategory = soundCategory
        this.distOrRoll = distOrRoll
        this.pitch = pitch
        this.keepTime = keepTime
    }

}