package de.themoep.dynamicslots.bungee;

import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/*
 * DynamicSlots - bungee
 * Copyright (C) 2022. Max Lee aka Phoenix616 (max@themoep.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

public class PlayerListener implements Listener {
    private final DynamicSlots plugin;

    public PlayerListener(DynamicSlots plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        int slots = plugin.getManager().getSlots(event.getResponse().getPlayers().getOnline(), event.getResponse().getPlayers().getMax());
        if (slots > -1) {
            event.getResponse().getPlayers().setMax(slots);
        }
    }

    @EventHandler
    public void onPlayerJoin(PreLoginEvent event) {
        if (event.isCancelled()) {
            if (event.getCancelReason().equals("Server is full!")) {
                event.setCancelReason(plugin.getKickMessage(plugin.getPlayerCount(), plugin.getSlotCount()));
            }
        } else {
            int slots = plugin.getManager().getSlots(plugin.getPlayerCount(), plugin.getSlotCount());
            if (plugin.getPlayerCount() + 1 >= slots) {
                event.setCancelled(true);
                event.setCancelReason(plugin.getKickMessage(plugin.getPlayerCount(), slots));
            }
        }
    }
}
