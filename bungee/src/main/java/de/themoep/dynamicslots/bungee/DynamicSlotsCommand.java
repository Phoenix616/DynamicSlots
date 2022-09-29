package de.themoep.dynamicslots.bungee;

/*
 * DynamicSlots - bungee
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

import de.themoep.bungeeplugin.BungeePlugin;
import de.themoep.bungeeplugin.PluginCommand;
import de.themoep.dynamicslots.core.DynamicSlotsCommandLogic;
import de.themoep.dynamicslots.core.DynamicSlotsPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class DynamicSlotsCommand extends PluginCommand {
    
    public DynamicSlotsCommand(BungeePlugin plugin, String name) {
        super(plugin, name);
    }
    
    @Override
    protected boolean run(CommandSender sender, String[] args) {
        return DynamicSlotsCommandLogic.execute((DynamicSlotsPlugin) plugin, sender instanceof ProxiedPlayer ? ((ProxiedPlayer) sender).getUniqueId() : null, args);
    }
}
