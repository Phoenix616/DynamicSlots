package de.themoep.dynamicslots.bukkit;

import de.themoep.dynamicslots.core.DynamicSlotsPlugin;
import de.themoep.dynamicslots.core.SlotManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

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

public final class DynamicSlots extends JavaPlugin implements DynamicSlotsPlugin {

    private SlotManager manager;

    @Override
    public void onEnable() {
        manager = new SlotManager(this);
        loadConfig();
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getCommand("dynamicslots").setExecutor(new DynamicSlotsCommand(this));
    }

    public boolean loadConfig() {
        saveDefaultConfig();
        reloadConfig();
        return manager.setupSource();
    }

    @Override
    public UUID getPlayerId(String playerName) {
        Player player = getServer().getPlayer(playerName);
        if (player != null) {
            return player.getUniqueId();
        }
        return null;
    }

    @Override
    public Object getSetting(String key) {
        return getConfig().get(key);
    }

    @Override
    public int getPlayerCount() {
        return getServer().getOnlinePlayers().size();
    }

    @Override
    public int getSlotCount() {
        return getServer().getMaxPlayers();
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
        if (getServer().isPrimaryThread()) {
            return getServer().getScheduler().runTaskAsynchronously(this, runnable).getTaskId();
        } else {
            runnable.run();
            return -1;
        }
    }

    @Override
    public String addColors(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @Override
    public void sendMessage(UUID playerId, String message) {
        message = addColors(message);
        if (playerId != null) {
            Player player = getServer().getPlayer(playerId);
            if (player != null) {
                player.sendMessage(message);
            }
        } else {
            getServer().getConsoleSender().sendMessage(message);
        }
    }

    @Override
    public boolean hasPermission(UUID playerId, String permission) {
        if (playerId != null) {
            Player player = getServer().getPlayer(playerId);
            if (player != null) {
                return player.hasPermission(permission);
            }
            return false;
        }
        return true;
    }
}
