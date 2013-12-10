/* COPYRIGHT (c) 2013 Deathmarine (Joshua McCurry)
 * This file is part of Ultrabans.
 * Ultrabans is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Ultrabans is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Ultrabans.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.modcrafting.ultrabans.commands;

import com.modcrafting.ultrabans.Language;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.modcrafting.ultrabans.Ultrabans;

public class Lockdown extends CommandHandler {
    public Lockdown(Ultrabans instance) {
        super(instance);
    }

    public String command(CommandSender sender, Command command, String[] args) {
        if (args.length < 1)
            return plugin.getString(Language.LOCKDOWN_ARGUMENTS);
        String admin = Ultrabans.DEFAULT_ADMIN;
        if (sender instanceof Player)
            admin = sender.getName();
        boolean locked = config.getBoolean("Lockdown", false);
        String toggle = args[0];
        if (toggle.equalsIgnoreCase("on")) {
            if (!locked) {
                plugin.getConfig().set("Lockdown", true);
                plugin.saveConfig();
                if (plugin.getLog())
                    plugin.getLogger().info(admin + " initiated lockdown.");
                return plugin.getString(Language.LOCKDOWN_START);
            }
            return plugin.getString(Language.LOCKDOWN_LOGINMSG);
        }
        if (toggle.equalsIgnoreCase("off")) {
            if (locked) {
                plugin.getConfig().set("Lockdown", false);
                plugin.saveConfig();
                if (plugin.getLog())
                    plugin.getLogger().info(admin + " disabled lockdown.");
                return plugin.getString(Language.LOCKDOWN_END);
            }
            return plugin.getString(Language.LOCKDOWN_STATUS);
        }
        return locked ? plugin.getString(Language.LOCKDOWN_LOGINMSG) : plugin.getString(Language.LOCKDOWN_STATUS);
    }
}
