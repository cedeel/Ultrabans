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
                if (victim.getPlayer().hasPermission("ultrabans.override.jail"))
                    return plugin.getString(Language.JAIL_DENIED);
                String msgToVictim = Formatting.replaceAmpersand(plugin.getString(Language.JAIL_MSGTOVICTIM));
                if (msgToVictim.contains(Formatting.ADMIN))
                    msgToVictim = msgToVictim.replace(Formatting.ADMIN, admin);
                if (msgToVictim.contains(Formatting.REASON))
                    msgToVictim = msgToVictim.replace(Formatting.REASON, reason);
                victim.getPlayer().sendMessage(msgToVictim);
                victim.getPlayer().teleport(plugin.jail.getJail("jail"));
            }
        }
        plugin.getAPI().jailPlayer(name, reason, admin);
        String broadcastMsg = Formatting.replaceAmpersand(plugin.getString(Language.JAIL_MSGTOBROADCAST));
        if (broadcastMsg.contains(Formatting.ADMIN))
            broadcastMsg = broadcastMsg.replace(Formatting.ADMIN, admin);
        if (broadcastMsg.contains(Formatting.REASON))
            broadcastMsg = broadcastMsg.replace(Formatting.REASON, reason);
        if (broadcastMsg.contains(Formatting.VICTIM))
            broadcastMsg = broadcastMsg.replace(Formatting.VICTIM, name);
        if (broadcast) {
            plugin.getServer().broadcastMessage(broadcastMsg);
        } else {
            sender.sendMessage(ChatColor.ITALIC + "Silent: " + broadcastMsg);
        }
        if (plugin.getLog())
            plugin.getLogger().info(ChatColor.stripColor(broadcastMsg));
        return null;
    }

}

        
