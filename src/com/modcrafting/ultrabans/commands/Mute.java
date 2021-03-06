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
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.modcrafting.ultrabans.Ultrabans;
import com.modcrafting.ultrabans.util.Formatting;

public class Mute extends CommandHandler {
    public Mute(Ultrabans instance) {
        super(instance);
    }

    public String command(CommandSender sender, Command command, String[] args) {
        if (args.length < 1)
            return plugin.getString(Language.MUTE_ARGUMENTS);
        String admin = Ultrabans.DEFAULT_ADMIN;
        String reason = Ultrabans.DEFAULT_REASON;
        if (sender instanceof Player)
            admin = sender.getName();
        String name = Formatting.expandName(args[0]);
        if (args.length > 1) {
            reason = Formatting.combineSplit(1, args);
        }
        if (name.equalsIgnoreCase(admin))
            return plugin.getString(Language.MUTE_EMO);
        Player victim = plugin.getServer().getPlayer(name);
        if (victim != null) {
            if (victim.hasPermission("ultrabans.override.mute"))
                return plugin.getString(Language.MUTE_DENIED);
            if (plugin.muted.contains(name.toLowerCase())) {
                plugin.muted.remove(name.toLowerCase());
                victim.sendMessage(Formatting.replaceAmpersand(plugin.getString(Language.MUTE_UNMUTEMSGTOVICTIM)));
                String adminMsgs = plugin.getString(Language.MUTE_UNMUTEMSGTOSENDER);
                if (adminMsgs.contains(Formatting.VICTIM))
                    adminMsgs = adminMsgs.replace(Formatting.VICTIM, name);
                return adminMsgs;
            }
            plugin.getAPI().mutePlayer(name, reason, admin);
            victim.sendMessage(Formatting.replaceAmpersand(plugin.getString(Language.MUTE_MUTEMSGTOVICTIM)));
            String adminMsgs = Formatting.replaceAmpersand(plugin.getString(Language.MUTE_MUTEMSGTOSENDER));
            if (adminMsgs.contains(Formatting.VICTIM))
                adminMsgs = adminMsgs.replace(Formatting.VICTIM, name);
            plugin.getLogger().info(ChatColor.stripColor(adminMsgs));
            return adminMsgs;
        }
        if (plugin.muted.contains(name.toLowerCase())) {
            plugin.muted.remove(name.toLowerCase());
            String adminMsgs = plugin.getString(Language.MUTE_UNMUTEMSGTOSENDER);
            if (adminMsgs.contains(Formatting.VICTIM))
                adminMsgs = adminMsgs.replace(Formatting.VICTIM, name);
            return adminMsgs;
        }
        return plugin.getString(Language.MUTE_FAILED);
    }

}
