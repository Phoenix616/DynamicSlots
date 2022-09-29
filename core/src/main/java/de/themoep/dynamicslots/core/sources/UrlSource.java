package de.themoep.dynamicslots.core.sources;
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

import de.themoep.dynamicslots.core.DynamicSlotsPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;

public class UrlSource extends SlotSource {

    public UrlSource(DynamicSlotsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void disable() {

    }

    @Override
    public int getSlots(int playerCount, int slotCount) {
        URL url = null;
        try {
            url = new URL(getQuery(playerCount, slotCount));
            url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder input = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                input.append(inputLine).append("\n");
            }
            in.close();
            return parseString(input.toString(), playerCount, slotCount);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Error while requesting data from " + url + "!", e);
        }
        return -1;
    }
}
