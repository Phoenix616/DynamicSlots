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

import de.themoep.dynamicslots.core.storage.FileSource;
import de.themoep.dynamicslots.core.storage.MySQLSource;
import de.themoep.dynamicslots.core.storage.SlotSource;
import de.themoep.dynamicslots.core.storage.StaticSource;
import de.themoep.dynamicslots.core.storage.UrlSource;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.logging.Level;

public class SlotManager {
    private final DynamicSlotsPlugin plugin;
    private SlotSource fallbackSource;
    private SlotSource source = null;
    private int slots = -1;
    private long lastUpdate = 0;
    private int cacheDuration = 60;

    public SlotManager(DynamicSlotsPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean setupSource() {
        disableSource();
        fallbackSource = new StaticSource(plugin);
        cacheDuration = (int) plugin.getSetting("cache-duration");
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

        return slots;
    }

    public boolean isManual() {
        return lastUpdate == -1;
    }

    public SlotSource getSource() {
        return source;
    }
}
