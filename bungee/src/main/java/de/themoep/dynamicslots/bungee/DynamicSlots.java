package de.themoep.dynamicslots.bungee;

import de.themoep.bungeeplugin.BungeePlugin;
import de.themoep.dynamicslots.core.DynamicSlotsPlugin;
import de.themoep.dynamicslots.core.SlotManager;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

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

public final class DynamicSlots extends BungeePlugin implements DynamicSlotsPlugin {

    private SlotManager manager;

    @Override
    public void onEnable() {
        manager = new SlotManager(this);
        loadConfig();
        getProxy().getPluginManager().registerListener(this, new PlayerListener(this));

        getProxy().getPluginManager().registerCommand(this, new DynamicSlotsCommand(this, "dynamicslots"));
    }

    public boolean loadConfig() {
        try {
            getConfig().createDefaultConfig();
            if (!getConfig().loadConfig()) {
                return false;
            }
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Error while loading config", e);
            return false;
        }
        return manager.setupSource();
    }

    @Override
    public UUID getPlayerId(String playerName) {
        ProxiedPlayer player = getProxy().getPlayer(playerName);
        if (player != null) {
            return player.getUniqueId();
        }
        return null;
    }

    @Override
    public Object getSetting(String key) {
        return getConfig().getConfiguration().get(key);
    }

    @Override
    public int getPlayerCount() {
        return getProxy().getOnlineCount();
    }

    @Override
    public int getSlotCount() {
        return getProxy().getConfig().getPlayerLimit();
    }

    @Override
    public void onDisable() {
        if (!isEnabled())
            return;

        manager.disable();
    }

    @Override
    public SlotManager getManager() {
        return manager;
    }

    @Override
    public int runAsync(Runnable runnable) {
        return getProxy().getScheduler().runAsync(this, runnable).getId();
    }

    @Override
    public String addColors(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @Override
    public void sendMessage(UUID playerId, String message) {
        sendMessage(playerId, TextComponent.fromLegacyText(addColors(message)));
    }

    private void sendMessage(UUID playerId, BaseComponent[] message) {
        if (playerId != null) {
            ProxiedPlayer player = getProxy().getPlayer(playerId);
            if (player != null) {
                player.sendMessage(message);
            }
        } else {
            getProxy().getConsole().sendMessage(message);
        }
    }

    @Override
    public boolean hasPermission(UUID playerId, String permission) {
        if (playerId != null) {
            ProxiedPlayer player = getProxy().getPlayer(playerId);
            if (player != null) {
                return player.hasPermission(permission);
            }
            return false;
        }
        return true;
    }
}
