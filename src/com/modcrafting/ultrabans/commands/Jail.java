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

public class Jail extends CommandHandler {
    public Jail(Ultrabans instance) {
        super(instance);
    }

    public String command(final CommandSender sender, final Command command, final String[] args) {
        if (args.length < 1)
            return plugin.getString(Language.JAIL_ARGUMENTS);
        if (args[0].equalsIgnoreCase("setjail")) {
            if (!(sender instanceof Player))
                return plugin.getString(Language.JAIL_SETFAIL);
            plugin.jail.setJail(((Player) sender).getLocation(), "jail");
            return plugin.getString(Language.JAIL_SETJAIL);
        }
        if (args[0].equalsIgnoreCase("setrelease")) {
            if (!(sender instanceof Player))
                return plugin.getString(Language.JAIL_SETJAIL);
            plugin.jail.setJail(((Player) sender).getLocation(), "release");
            return plugin.getString(Language.JAIL_SETRELEASE);
        }
        boolean broadcast = true;
        String admin = Ultrabans.DEFAULT_ADMIN;
        String reason = Ultrabans.DEFAULT_REASON;
        if (sender instanceof Player)
            admin = sender.getName();
        String name = Formatting.expandName(args[0]);
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
        if (name.equalsIgnoreCase(admin))
            return plugin.getString(Language.JAIL_EMO);
        if (plugin.cache.containsKey(name.toLowerCase())) {
            for (BanInfo info : plugin.cache.get(name.toLowerCase())) {
                if (info.getType() == BanType.JAIL.getId()) {
                    String msg = plugin.getString(Language.JAIL_FAILED);
                    if (msg.contains(Formatting.VICTIM))
                        msg = msg.replace(Formatting.VICTIM, name);
                    return msg;
                }
            }
        }
        OfflinePlayer victim = plugin.getServer().getOfflinePlayer(name);
        if (victim != null) {
            name = victim.getName();
            if (victim.isOnline()) {
                if (victim.getPlayer().hasPermission("ultraban.override.jail"))
                    return plugin.getString(Language.JAIL_DENIED);
                String msgvic = plugin.getString(Language.JAIL_MSGTOVICTIM);
                if (msgvic.contains(Formatting.ADMIN))
                    msgvic = msgvic.replace(Formatting.ADMIN, admin);
                if (msgvic.contains(Formatting.REASON))
                    msgvic = msgvic.replace(Formatting.REASON, reason);
                victim.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', msgvic));
                victim.getPlayer().teleport(plugin.jail.getJail("jail"));
            }
        }
        String bcmsg = plugin.getString(Language.JAIL_MSGTOBROADCAST);
        if (bcmsg.contains(Formatting.ADMIN))
            bcmsg = bcmsg.replace(Formatting.ADMIN, admin);
        if (bcmsg.contains(Formatting.REASON))
            bcmsg = bcmsg.replace(Formatting.REASON, reason);
        if (bcmsg.contains(Formatting.VICTIM))
            bcmsg = bcmsg.replace(Formatting.VICTIM, name);
        if (broadcast) {
            plugin.getServer().broadcastMessage(bcmsg);
        } else {
            sender.sendMessage(ChatColor.ITALIC + "Silent: " + bcmsg);
        }
        if (plugin.getLog())
            plugin.getLogger().info(ChatColor.stripColor(bcmsg));
        return null;
    }

}

        
