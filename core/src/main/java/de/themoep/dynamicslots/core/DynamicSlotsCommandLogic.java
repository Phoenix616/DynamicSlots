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

            if (plugin.hasPermission(senderId, "dynamicslots.command." + args[0].toLowerCase())) {
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
                plugin.sendMessage(senderId, "&eSlots: &f" + plugin.getManager().getSlots());
                plugin.sendMessage(senderId, "&eManual: &f" + plugin.getManager().isManual());
                if (plugin.getManager().getSource() == null) {
                    plugin.sendMessage(senderId, "&eSource: &cStatic");
                } else {
                    plugin.sendMessage(senderId, "&eSource: &f" + plugin.getManager().getSource().getClass().getSimpleName().replace("Source", ""));
                    plugin.sendMessage(senderId, "&eQuery: &f" + plugin.getManager().getSource().getQuery());
                    plugin.sendMessage(senderId, "&eRegex: &f" + plugin.getManager().getSource().getPattern());
                }

            }
            return true;
        }
        return false;
    }
}
