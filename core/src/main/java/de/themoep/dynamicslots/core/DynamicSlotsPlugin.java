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

import java.util.UUID;
import java.util.logging.Logger;

public interface DynamicSlotsPlugin {

    Logger getLogger();

    SlotManager getManager();

    boolean isEnabled();

    int runAsync(Runnable runnable);

    String addColors(String string);

    void sendMessage(UUID playerId, String message);

    boolean hasPermission(UUID playerId, String permission);

    boolean loadConfig();

    UUID getPlayerId(String playerName);

    Object getSetting(String key);

    int getPlayerCount();

    int getSlotCount();

    default String getKickMessage(int playerCount, int slotCount) {
        return addColors(replaceVariables((String) getSetting("full-kick-message"), playerCount, slotCount));
    }

    default String replaceVariables(String string, int playerCount, int slotCount) {
        return string.replace("%players%", String.valueOf(playerCount)).replace("%slots%", String.valueOf(slotCount));
    }
}
