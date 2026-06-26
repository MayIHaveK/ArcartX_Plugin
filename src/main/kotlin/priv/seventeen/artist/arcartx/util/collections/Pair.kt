/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.util.collections

data class Pair<A, B>(val first: A, val second: B) {
    companion object {
        @JvmStatic
        fun <A, B> of(a: A, b: B): Pair<A, B> {
            return Pair(a, b)
        }
    }
}