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

public class Kick extends CommandHandler {
    public Kick(Ultrabans instance) {
        super(instance);
    }

    public String command(CommandSender sender, Command command, String[] args) {
        if (args.length < 1)
            return plugin.getString(Language.KICK_ARGUMENTS);
        String admin = Ultrabans.DEFAULT_ADMIN;
        String reason = Ultrabans.DEFAULT_REASON;
        boolean broadcast = true;
        if (sender instanceof Player)
            admin = sender.getName();
        if ((args[0].equals("*") || args[0].equals("all")) && sender.hasPermission("ultrabans.kick.all")) {
            if (args.length > 1)
                reason = Formatting.combineSplit(1, args);
            String adminMsg = Formatting.replaceAmpersand(plugin.getString(Language.KICK_MSGTOALL));
            if (adminMsg.contains(Formatting.ADMIN))
                adminMsg = adminMsg.replace(Formatting.ADMIN, admin);
            if (adminMsg.contains(Formatting.REASON))
                adminMsg = adminMsg.replace(Formatting.REASON, reason);
            for (Player players : plugin.getServer().getOnlinePlayers()) {
                if (!players.hasPermission("ultrabans.override.kick.all")) {
                    players.kickPlayer(adminMsg);
                }
            }
            plugin.log(adminMsg);
            return adminMsg;
        }
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
        Player victim = plugin.getServer().getPlayer(name);
        if (victim == null)
            return plugin.getString(Language.KICK_ONLINE);
        if (victim.getName().equalsIgnoreCase(admin))
            return plugin.getString(Language.KICK_EMO);
        if (victim.hasPermission("ultrabans.override.kick"))
            return plugin.getString(Language.KICK_DENIED);
        plugin.getAPI().kickPlayer(name, reason, admin);
        String msgvic = Formatting.replaceAmpersand(plugin.getString(Language.KICK_MSGTOVICTIM));
        if (msgvic.contains(Formatting.ADMIN))
            msgvic = msgvic.replace(Formatting.ADMIN, admin);
        if (msgvic.contains(Formatting.REASON))
            msgvic = msgvic.replace(Formatting.REASON, reason);
        victim.kickPlayer(msgvic);
        String bcmsg = Formatting.replaceAmpersand(plugin.getString(Language.KICK_MSGTOBROADCAST));
        if (bcmsg.contains(Formatting.ADMIN))
            bcmsg = bcmsg.replace(Formatting.ADMIN, admin);
        if (bcmsg.contains(Formatting.REASON))
            bcmsg = bcmsg.replace(Formatting.REASON, reason);
        if (bcmsg.contains(Formatting.VICTIM))
            bcmsg = bcmsg.replace(Formatting.VICTIM, victim.getName());
        if (broadcast) {
            plugin.getServer().broadcastMessage(bcmsg);
        } else {
            sender.sendMessage(ChatColor.ITALIC + "Silent: " + bcmsg);
            plugin.log(bcmsg);
        }
        return null;
    }
}
