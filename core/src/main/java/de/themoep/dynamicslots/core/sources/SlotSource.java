package de.themoep.dynamicslots.core.sources;
/*
 * Copyright 2016 Max Lee (https://github.com/Phoenix616/)
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

import de.themoep.dynamicslots.core.DynamicSlotsPlugin;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SlotSource {
    protected final DynamicSlotsPlugin plugin;
    private final Pattern pattern;
    private String query;

    protected SlotSource(DynamicSlotsPlugin plugin) {
        plugin.getLogger().info("Loading " + getClass().getSimpleName() + "...");
        this.plugin = plugin;
        this.query = (String) plugin.getSetting("source.query");
        if (plugin.getSetting("source.regex") != null && !((String) plugin.getSetting("source.regex")).isEmpty()) {
            pattern = Pattern.compile((String) plugin.getSetting("source.regex"));
        } else {
            pattern = null;
        }
    }

    public void disable() {

    }

    /**
     * Get the amount of slots that should be displayed
     * @param playerCount   The amount of players that are online
     * @param slotCount     The amount of slots the server has
     * @return  The amount of players
     */
    public abstract int getSlots(int playerCount, int slotCount);

    protected int parseString(String s, int playerCount, int slotCount) {
        try {
            return Integer.parseInt(applyRegex(plugin.replaceVariables(s, playerCount, slotCount)));
        } catch (NumberFormatException e) {
            plugin.getLogger().log(Level.WARNING, "Could not parse " + s + " as an integer!");
        }
        return -1;
    }

    protected String applyRegex(String s) {
        if (pattern != null) {
            Matcher m = pattern.matcher(s);
            if (m.find()) {
                s = m.group(1);
            }
        }
        return s;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getQuery(int playerCount, int slotCount) {
        return plugin.replaceVariables(getQuery(), playerCount, slotCount);
    }

    public String getQuery() {
        return query;
    }

}
