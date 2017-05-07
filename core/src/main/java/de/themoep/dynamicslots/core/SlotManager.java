package de.themoep.dynamicslots.core;
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
    private int maxSlots = Integer.MAX_VALUE;

    public SlotManager(DynamicSlotsPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean setupSource() {
        disableSource();
        fallbackSource = new StaticSource(plugin);
        moreSource = new MoreSource(plugin);
        cacheDuration = (int) plugin.getSetting("cache-duration");
        maxSlots = (int) plugin.getSetting("max-slots");
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
            try {
                source = new UrlSource(plugin);
            } catch (MalformedURLException e) {
                plugin.getLogger().log(Level.SEVERE, "Error while initializing UrlSource! Plugin will not dynamically calculate slots!", e);
                return false;
            }
        } else if (!"static".equals(type)) {
            plugin.getLogger().log(Level.WARNING, "Unknown source type " + type + "! Plugin will not dynamically calculate slots!");
            return false;
        }
        updateSlots();
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
        updateSlots();
    }

    public void updateSlots() {
        lastUpdate = System.currentTimeMillis();
        if (source == null) {
            slots = fallbackSource.getSlots();
        } else {
            plugin.runAsync(() -> {
                slots = source.getSlots();
            });
        }
    }

    public int getSlots() {
        if (source != null && lastUpdate > -1 && lastUpdate + cacheDuration * 1000 < System.currentTimeMillis()) {
            updateSlots();
        }

        int slots = this.slots;
        if (slots <= plugin.getPlayerCount() && moreSource.getSlots() != 0) {
            slots = moreSource.getSlots();
        }
        if (maxSlots > -1 && slots > maxSlots) {
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

    public int getMaxSlots() {
        return maxSlots;
    }
}
