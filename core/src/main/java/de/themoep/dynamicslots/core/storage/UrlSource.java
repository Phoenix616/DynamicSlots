package de.themoep.dynamicslots.core.storage;
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

import de.themoep.dynamicslots.core.DynamicSlotsPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

public class UrlSource extends SlotSource {
    private final URL url;

    public UrlSource(DynamicSlotsPlugin plugin) throws MalformedURLException {
        super(plugin);
        url = new URL(getQuery());
    }


    @Override
    public void disable() {

    }

    @Override
    public int getSlots() {
        try {
            url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder input = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                input.append(inputLine).append("\n");
            }
            in.close();
            return parseString(input.toString());
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Error while requesting data from " + url + "!", e);
        }
        return -1;
    }
}
