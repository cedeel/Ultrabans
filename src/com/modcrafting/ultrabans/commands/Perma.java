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
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.modcrafting.ultrabans.Ultrabans;
import com.modcrafting.ultrabans.util.BanInfo;
import com.modcrafting.ultrabans.util.BanType;
import com.modcrafting.ultrabans.util.Formatting;

public class Perma extends CommandHandler {
    public Perma(Ultrabans instance) {
        super(instance);
    }

    public String command(CommandSender sender, Command command, String[] args) {
        if (args.length < 1)
            return plugin.getString(Language.PERMABAN_ARGUMENTS);
        boolean broadcast = true;
        String admin = Ultrabans.DEFAULT_ADMIN;
        String reason = Ultrabans.DEFAULT_REASON;
        if (sender instanceof Player)
            admin = sender.getName();
        String name = args[0];
        name = Formatting.expandName(name);
        if (name.equalsIgnoreCase(admin))
            return plugin.getString(Language.PERMABAN_EMO);
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("-s")) {
                if (sender.hasPermission(command.getPermission() + ".silent"))
                    broadcast = false;
                reason = Formatting.combineSplit(2, args);
            } else if (args[1].equalsIgnoreCase("-a")) {
                if (sender.hasPermission(command.getPermission() + ".anon"))
                    admin = Ultrabans.DEFAULT_ADMIN;
                reason = Formatting.combineSplit(2, args);
            } else {
                reason = Formatting.combineSplit(1, args);
            }
        }
        if (plugin.cache.containsKey(name.toLowerCase())) {
            for (BanInfo info : plugin.cache.get(name.toLowerCase())) {
                if (info.getType() == BanType.BAN) {
                    String failed = plugin.getString(Language.PERMABAN_FAILED);
                    if (failed.contains(Formatting.VICTIM))
                        failed = failed.replace(Formatting.VICTIM, name);
                    return failed;
                }
            }
        }
        OfflinePlayer victim = plugin.getServer().getOfflinePlayer(name);
        if (victim != null) {
            if (victim.isOnline()) {
                if (victim.getPlayer().hasPermission("ultrabans.override.permaban") &&
                        !admin.equalsIgnoreCase(Formatting.ADMIN))
                    return plugin.getString(Language.PERMABAN_DENIED);
                String vicmsg = plugin.getString(Language.PERMABAN_MSGTOVICTIM);
                if (vicmsg.contains(Formatting.ADMIN))
                    vicmsg = vicmsg.replace(Formatting.ADMIN, admin);
                if (vicmsg.contains(Formatting.REASON))
                    vicmsg = vicmsg.replace(Formatting.REASON, reason);
                victim.getPlayer().kickPlayer(Formatting.replaceAmpersand(vicmsg));
            }
            name = victim.getName();
        }
        plugin.getAPI().permabanPlayer(name, reason, admin);
        String bcmsg = Formatting.replaceAmpersand(plugin.getString(Language.PERMABAN_MSGTOBROADCAST));
        if (bcmsg.contains(Formatting.ADMIN))
            bcmsg = bcmsg.replace(Formatting.ADMIN, admin);
        if (bcmsg.contains(Formatting.REASON))
            bcmsg = bcmsg.replace(Formatting.REASON, reason);
        if (bcmsg.contains(Formatting.VICTIM))
            bcmsg = bcmsg.replace(Formatting.VICTIM, name);
        if (config.getBoolean("CleanOnBan"))
            Formatting.deletePlyrdat(name);
        if (config.getBoolean("ClearWarnOnBan", false))
            plugin.getAPI().clearWarn(name);
        if (broadcast) {
            plugin.getServer().broadcastMessage(bcmsg);
        } else {
            sender.sendMessage(ChatColor.ITALIC + "Silent: " + bcmsg);
            plugin.log(bcmsg);
        }
        return null;
    }
}
