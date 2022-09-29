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

public class StaticSource extends SlotSource {
    private final int slots;

    public StaticSource(DynamicSlotsPlugin plugin) {
        super(plugin);
        slots = (int) plugin.getSetting("static-slots");
    }

    @Override
    public int getSlots(int playerCount, int slotCount) {
        return slots;
    }
}
