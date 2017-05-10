package de.themoep.dynamicslots.bungee;

import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

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
