package de.themoep.dynamicslots.bukkit;

/*
 * Copyright 2017 Max Lee (https://github.com/Phoenix616/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Mozilla Public License as published by
 * the Mozilla Foundation, version 2.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License v2.0 for more details.
 *
 * You should have received a copy of the Mozilla Public License v2.0
 * along with this program. If not, see <http://mozilla.org/MPL/2.0/>.
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
