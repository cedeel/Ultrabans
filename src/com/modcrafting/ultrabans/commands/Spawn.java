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
import com.modcrafting.ultrabans.util.Formatting;

public class Spawn extends CommandHandler {
    public Spawn(Ultrabans instance) {
        super(instance);
    }

    public String command(final CommandSender sender, Command command, String[] args) {
        if (args.length < 1)
            return plugin.getString(Language.SPAWN_ARGUMENTS);
        String admin = Ultrabans.DEFAULT_ADMIN;
        if (sender instanceof Player)
            admin = sender.getName();
        String name = Formatting.expandName(args[0]);
        Player victim = plugin.getServer().getPlayer(name);
        if (victim == null) {
            return plugin.getString(Language.SPAWN_FAILED);
        }
        victim.sendMessage(Formatting.replaceAmpersand(plugin.getString(Language.SPAWN_MSGTOVICTIM)));
        victim.teleport(victim.getWorld().getSpawnLocation());

        String vicmsg = plugin.getString(Language.SPAWN_MSGTOSENDER);
        if (vicmsg.contains(Formatting.ADMIN))
            vicmsg = vicmsg.replace(Formatting.ADMIN, admin);
        if (vicmsg.contains(Formatting.VICTIM))
            vicmsg = vicmsg.replace(Formatting.VICTIM, name);
        plugin.log(vicmsg);
        return vicmsg;
    }
}
