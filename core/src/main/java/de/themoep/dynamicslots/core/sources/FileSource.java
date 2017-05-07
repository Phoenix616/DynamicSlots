package de.themoep.dynamicslots.core.sources;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

public class FileSource extends SlotSource {
    private final File file;

    public FileSource(DynamicSlotsPlugin plugin) {
        super(plugin);
        file = new File(getQuery());
    }

    @Override
    public int getSlots() {
        if (file.exists() && file.isFile() && file.canRead()) {
            try {
                String s = new String(Files.readAllBytes(file.toPath()));
                return parseString(s);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Error while getting slots from file!", e);
            }
        }
        return -1;
    }
}
