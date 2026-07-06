/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.hook.trmenu;

import me.arasple.mc.trmenu.api.receptacle.Receptacle;
import me.arasple.mc.trmenu.api.receptacle.ReceptacleAPIKt;
import me.arasple.mc.trmenu.api.receptacle.ReceptacleClickType;
import me.arasple.mc.trmenu.api.receptacle.ReceptacleInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;

/**
 * TrMenu 交互的 Java 侧实现。
 */
final class TrMenuApi {

    private TrMenuApi() {
    }

    /**
     * 将一次容器点击注入 TrMenu 的 Receptacle 点击管线。
     *
     * @return {@code true} 表示玩家正在查看 TrMenu 菜单、点击已被消费；
     * {@code false} 表示玩家未在查看 TrMenu 菜单，调用方应走原有逻辑。
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    static boolean route(Player player, int slot, ClickType click, InventoryAction action) {
        Receptacle receptacle = ReceptacleAPIKt.getViewingReceptacle(player);
        if (receptacle == null) {
            return false;
        }
        ReceptacleClickType clickType = ReceptacleClickType.Companion.from(click, action, slot);
        if (clickType == null) {
            return true;
        }
        receptacle.callEventClick(new ReceptacleInteractEvent(player, receptacle, clickType, slot));
        return true;
    }
}
