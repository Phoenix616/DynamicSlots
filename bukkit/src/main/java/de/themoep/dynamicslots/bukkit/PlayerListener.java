package de.themoep.dynamicslots.bukkit;

/*
 * DynamicSlots - bukkit
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

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class PlayerListener implements Listener {
    private final DynamicSlots plugin;

    public PlayerListener(DynamicSlots plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        int slots = plugin.getManager().getSlots(event.getNumPlayers(), event.getMaxPlayers());
        if (slots > -1) {
            event.setMaxPlayers(slots);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            if (event.getPlayer().hasPermission("dynamicslots.joinfull")) {
                event.setResult(PlayerLoginEvent.Result.ALLOWED);
            } else {
                event.setKickMessage(plugin.getKickMessage(plugin.getPlayerCount(), plugin.getSlotCount()));
            }
        } else if (event.getResult() == PlayerLoginEvent.Result.ALLOWED && !event.getPlayer().hasPermission("dynamicslots.joinfull")) {
            int slots = plugin.getManager().getSlots(plugin.getPlayerCount(), plugin.getSlotCount());
            if (plugin.getPlayerCount() + 1 >= slots) {
                event.setResult(PlayerLoginEvent.Result.KICK_FULL);
                event.setKickMessage(plugin.getKickMessage(plugin.getPlayerCount(), slots));
            }
        }
    }
}
