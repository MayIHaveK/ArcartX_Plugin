/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.hook.mythicmobs.hitbox;

import io.lumine.mythic.core.mobs.MobType;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

/**
 * 通过 VarHandle 访问 MythicMobs MobType 的私有字段 applyInvisibility，
 * 用于在不安装 ModelEngine 时控制 MythicMobs 实体的隐身属性
 */
public class Accessor {

    private static final VarHandle APPLY_INVISIBILITY;

    static {
        try {
            MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(
                    MobType.class,
                    MethodHandles.lookup()
            );
            APPLY_INVISIBILITY = lookup.findVarHandle(MobType.class, "applyInvisibility", Boolean.class);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void setApplyInvisibility(MobType mobType, boolean applyInvisibility) {
        APPLY_INVISIBILITY.set(mobType, applyInvisibility);
    }
}
