package de.themoep.dynamicslots.bungee;

import de.themoep.bungeeplugin.BungeePlugin;
import de.themoep.bungeeplugin.PluginCommand;
import de.themoep.dynamicslots.core.DynamicSlotsCommandLogic;
import de.themoep.dynamicslots.core.DynamicSlotsPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
public class DynamicSlotsCommand extends PluginCommand {
    
    public DynamicSlotsCommand(BungeePlugin plugin, String name) {
        super(plugin, name);
    }
    
    @Override
    protected boolean run(CommandSender sender, String[] args) {
        return DynamicSlotsCommandLogic.execute((DynamicSlotsPlugin) plugin, sender instanceof ProxiedPlayer ? ((ProxiedPlayer) sender).getUniqueId() : null, args);
    }
}
