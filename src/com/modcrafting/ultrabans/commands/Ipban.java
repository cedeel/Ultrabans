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

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.modcrafting.ultrabans.Ultrabans;
import com.modcrafting.ultrabans.util.Formatting;

public class Ipban extends CommandHandler {
    public Ipban(Ultrabans instance) {
        super(instance);
    }

    public String command(CommandSender sender, Command command, String[] args) {
        if (args.length < 1)
            return lang.getString("IPBan.Arguments");
        boolean broadcast = true;
        String admin = Ultrabans.DEFAULT_ADMIN;
        String reason = Ultrabans.DEFAULT_REASON;
        if (sender instanceof Player)
            admin = sender.getName();
        String name = Formatting.expandName(args[0]);
        if (name.equalsIgnoreCase(admin))
            return lang.getString("IPBan.Emo");
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
        if (Formatting.validIP(name)) {
            String pname = plugin.getUBDatabase().getName(name);
            if (pname == null)
                pname = name;
            plugin.getAPI().ipbanPlayer(pname, name, reason, admin);
            String bcmsg = lang.getString("IPBan.MsgToBroadcast");
            if (bcmsg.contains(Formatting.ADMIN)) bcmsg = bcmsg.replace(Formatting.ADMIN, admin);
            if (bcmsg.contains(Formatting.REASON)) bcmsg = bcmsg.replace(Formatting.REASON, reason);
            if (bcmsg.contains(Formatting.VICTIM)) bcmsg = bcmsg.replace(Formatting.VICTIM, name);
            if (broadcast) {
                plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', bcmsg));
            } else {
                sender.sendMessage(ChatColor.ITALIC + "Silent: " + ChatColor.translateAlternateColorCodes('&', bcmsg));
            }
            if (plugin.getLog())
                plugin.getLogger().info(admin + " banned player " + name + ".");
            return null;
        }
        String victimip = plugin.getUBDatabase().getAddress(name);
        OfflinePlayer victim = plugin.getServer().getOfflinePlayer(name);
        if (victim != null) {
            if (victim.isOnline()) {
                if (victim.getPlayer().hasPermission("ultraban.override.ipban")
                        && !admin.equalsIgnoreCase(Ultrabans.DEFAULT_ADMIN))
                    return lang.getString("IPBan.Denied");
                victimip = victim.getPlayer().getAddress().getAddress().getHostAddress();
                plugin.getUBDatabase().setAddress(victim.getName(), victimip);
            }
            name = victim.getName();
            if (victimip == null)
                victimip = plugin.getUBDatabase().getAddress(victim.getName());
        }
        if (victimip == null) {
            String failed = lang.getString("IPBan.IPNotFound");
            if (failed.contains(Formatting.VICTIM))
                failed = failed.replace(Formatting.VICTIM, name);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', failed));
            StringBuilder sb = new StringBuilder();
            sb.append("ban").append(" ").append(name).append(" ");
            if (!broadcast)
                sb.append("-s").append(" ");
            sb.append(reason);
            plugin.getServer().dispatchCommand(sender, sb.toString());
            return null;
        }
        if (plugin.cacheIP.containsKey(victimip)) {
            String failed = lang.getString("IPBan.Failed");
            if (failed.contains(Formatting.VICTIM))
                failed = failed.replace(Formatting.VICTIM, name);
            return failed;
        }
        plugin.getAPI().ipbanPlayer(name, victimip, reason, admin);
        if (victim != null && victim.isOnline()) {
            String msgvic = lang.getString("IPBan.MsgToVictim");
            if (msgvic.contains(Formatting.ADMIN))
                msgvic = msgvic.replace(Formatting.ADMIN, admin);
            if (msgvic.contains(Formatting.REASON))
                msgvic = msgvic.replace(Formatting.REASON, reason);
            victim.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', msgvic));
        }
        String bcmsg = ChatColor.translateAlternateColorCodes('&', lang.getString("IPBan.MsgToBroadcast"));
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
        }
        if (plugin.getLog())
            plugin.getLogger().info(ChatColor.stripColor(bcmsg));
        return null;
    }
}
