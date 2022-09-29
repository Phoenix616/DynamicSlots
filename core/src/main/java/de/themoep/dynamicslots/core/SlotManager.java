package de.themoep.dynamicslots.core;
/*
 * DynamicSlots - core
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

import de.themoep.dynamicslots.core.sources.FileSource;
import de.themoep.dynamicslots.core.sources.MoreSource;
import de.themoep.dynamicslots.core.sources.MySQLSource;
import de.themoep.dynamicslots.core.sources.SlotSource;
import de.themoep.dynamicslots.core.sources.StaticSource;
import de.themoep.dynamicslots.core.sources.UrlSource;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.logging.Level;

public class SlotManager {
    private final DynamicSlotsPlugin plugin;
    private SlotSource fallbackSource;
    private SlotSource source = null;
    private MoreSource moreSource;
    private int slots = -1;
    private long lastUpdate = 0;
    private int cacheDuration = 60;
    private int minSlots = 0;
    private int maxSlots = Integer.MAX_VALUE;

    public SlotManager(DynamicSlotsPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean setupSource() {
        disableSource();
        fallbackSource = new StaticSource(plugin);
        moreSource = new MoreSource(plugin);
        cacheDuration = (int) plugin.getSetting("cache-duration");
        minSlots = (int) plugin.getSetting("min-slots");
        if (plugin.getSlotCount() > -1 && minSlots > plugin.getSlotCount()) {
            plugin.getLogger().log(Level.WARNING, "Configured minimum slot amount " + minSlots + " is above the server's slot amount of " + plugin.getSlotCount() + "?");
            minSlots = plugin.getSlotCount();
        }
        maxSlots = (int) plugin.getSetting("max-slots");
        if (plugin.getSlotCount() > -1 && maxSlots > plugin.getSlotCount()) {
            plugin.getLogger().log(Level.WARNING, "Configured maximum slot amount " + maxSlots + " is above the server's slot amount of " + plugin.getSlotCount() + "?");
            maxSlots = plugin.getSlotCount();
        }
        String type = ((String) plugin.getSetting("source.type")).toLowerCase();
        if ("mysql".equals(type)) {
            try {
                source = new MySQLSource(plugin);
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Error while initializing MySQLSource! Plugin will not dynamically calculate slots!", e);
                return false;
            }
        } else if ("file".equals(type)) {
            source = new FileSource(plugin);
        } else if ("url".equals(type)) {
            source = new UrlSource(plugin);
        } else if (!"static".equals(type)) {
            plugin.getLogger().log(Level.WARNING, "Unknown source type " + type + "! Plugin will not dynamically calculate slots!");
            return false;
        }
        updateSlots(plugin.getPlayerCount(), plugin.getSlotCount());
        return true;
    }

    public void disable() {
        disableSource();
    }

    private void disableSource() {
        if (source != null) {
            source.disable();
        }
    }

    public void setSlots(int slots) {
        this.slots = slots;
        lastUpdate = -1;
    }

    public void resetSlots() {
        updateSlots(plugin.getPlayerCount(), plugin.getSlotCount());
    }

    public void updateSlots(int playerCount, int slotCount) {
        lastUpdate = System.currentTimeMillis();
        if (source == null) {
            slots = fallbackSource.getSlots(playerCount, slotCount);
        } else {
            plugin.runAsync(() -> {
                slots = source.getSlots(playerCount, slotCount);
            });
        }
    }

    public int getSlots(int playerCount, int slotCount) {
        if (source != null && lastUpdate > -1 && lastUpdate + cacheDuration * 1000 < System.currentTimeMillis()) {
            updateSlots(playerCount, slotCount);
        }

        int slots = this.slots;
        if (slots <= playerCount && moreSource.getSlots(playerCount, slotCount) != 0) {
            slots = moreSource.getSlots(playerCount, slotCount);
        }
        if (slots > 0 && slots < minSlots) {
            slots = minSlots;
        } else if (maxSlots > -1 && slots > maxSlots) {
            slots = maxSlots;
        }
        return slots;
    }

    public boolean isManual() {
        return lastUpdate == -1;
    }

    public SlotSource getSource() {
        return source;
    }

    public MoreSource getMoreSource() {
        return moreSource;
    }

    public int getMinSlots() {
        return minSlots;
    }

    public int getMaxSlots() {
        return maxSlots;
    }

    public int getCacheDuration() {
        return cacheDuration;
    }
}
