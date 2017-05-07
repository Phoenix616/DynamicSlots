package de.themoep.dynamicslots.core.storage;
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

public class StaticSource extends SlotSource {
    private final int slots;

    public StaticSource(DynamicSlotsPlugin plugin) {
        super(plugin);
        slots = (int) plugin.getSetting("static-slots");
    }

    @Override
    public int getSlots() {
        return slots;
    }
}
