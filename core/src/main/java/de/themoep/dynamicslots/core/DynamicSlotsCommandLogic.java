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

public class DynamicSlotsCommandLogic {

    private static final String NO_PERMISSION = "&cYou don't have the permission &e%s&c!";

    /**
     * Execute the command
     * @param senderId  The UUID of the sender or null if it is the console
     * @param args      The command arguments
     * @return          Whether or not it executed successfully
     */
    public static boolean execute(DynamicSlotsPlugin plugin, UUID senderId, String[] args) {
        if (args.length > 0) {
            if (!"reload|set|reset|info".contains(args[0].toLowerCase())) {
                return false;
            }

            if (!plugin.hasPermission(senderId, "dynamicslots.command." + args[0].toLowerCase())) {
                plugin.sendMessage(senderId, String.format(NO_PERMISSION, "dynamicslots.command." + args[0].toLowerCase()));
                return true;
            }

            if ("reload".equalsIgnoreCase(args[0])) {
                if (plugin.loadConfig()) {
                    plugin.sendMessage(senderId, "&aConfig reloaded!");
                } else {
                    plugin.sendMessage(senderId, "&cError while reloading the config!");
                }

            } else if ("set".equalsIgnoreCase(args[0])) {
                if (args.length < 2) {
                    plugin.sendMessage(senderId, "&cMissing parameter: &e/dynamicslots set <slots>");
                    return true;
                }

                try {
                    int slots = Integer.parseInt(args[1]);
                    plugin.getManager().setSlots(slots);
                    plugin.sendMessage(senderId, "&eSet slots to " + slots + "!");
                } catch (NumberFormatException e) {
                    plugin.sendMessage(senderId, "&cError: &e" + args[1] + " &cis not a valid number input!");
                }

            } else if ("reset".equalsIgnoreCase(args[0])) {
                plugin.getManager().resetSlots();
                plugin.sendMessage(senderId, "&eReset slots to configured source!");

            } else if  ("info".equalsIgnoreCase(args[0])) {
                plugin.sendMessage(senderId, "&eSlots: &f" + plugin.getManager().getSlots(plugin.getPlayerCount(), plugin.getSlotCount()));
                plugin.sendMessage(senderId, "&eManual: &f" + plugin.getManager().isManual());
                if (plugin.getManager().getSource() == null) {
                    plugin.sendMessage(senderId, "&eSource: &cStatic");
                } else {
                    plugin.sendMessage(senderId, "&eSource: &f" + plugin.getManager().getSource().getClass().getSimpleName().replace("Source", ""));
                    plugin.sendMessage(senderId, "&eQuery: &f" + plugin.getManager().getSource().getQuery());
                    plugin.sendMessage(senderId, "&eRegex: &f" + plugin.getManager().getSource().getPattern());
                }
                plugin.sendMessage(senderId, "&eAdd when full: &f" + plugin.getManager().getMoreSource().getAdd());
                plugin.sendMessage(senderId, "&eMin Slots: &f" + plugin.getManager().getMinSlots());
                plugin.sendMessage(senderId, "&eMax Slots: &f" + plugin.getManager().getMaxSlots());
                plugin.sendMessage(senderId, "&eCache duration: &f" + plugin.getManager().getCacheDuration() + "s");

            }
            return true;
        }
        return false;
    }
}
