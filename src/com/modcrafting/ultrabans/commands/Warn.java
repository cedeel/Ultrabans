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
import com.modcrafting.ultrabans.util.Formatting;

public class Warn extends CommandHandler {
    public Warn(Ultrabans instance) {
        super(instance);
    }

    public String command(CommandSender sender, Command command, String[] args) {
        if (args.length < 1)
            return plugin.getString(Language.WARN_ARGUMENTS);
        boolean broadcast = true;
        String admin = Ultrabans.DEFAULT_ADMIN;
        String reason = Ultrabans.DEFAULT_REASON;
        if (sender instanceof Player)
            admin = sender.getName();
        String name = Formatting.expandName(args[0]);
        if (name.equalsIgnoreCase(admin))
            return plugin.getString(Language.WARN_EMO);
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
        OfflinePlayer victim = plugin.getServer().getOfflinePlayer(name);
        if (victim != null) {
            if (victim.isOnline()) {
                if (victim.getPlayer().hasPermission("ultrabans.override.warn") &&
                        !admin.equalsIgnoreCase(Formatting.ADMIN))
                    return plugin.getString(Language.WARN_DENIED);
                String vicmsg = plugin.getString(Language.WARN_MSGTOVICTIM);
                if (vicmsg.contains(Formatting.ADMIN))
                    vicmsg = vicmsg.replace(Formatting.ADMIN, admin);
                if (vicmsg.contains(Formatting.REASON))
                    vicmsg = vicmsg.replace(Formatting.REASON, reason);
                victim.getPlayer().sendMessage(Formatting.replaceAmpersand(vicmsg));

            }
            name = victim.getName();
        }
        if (config.getBoolean("MaxWarning.Enable", false)) {
            Integer max = config.getInt("MaxWarning.Amt", 5);
            String idoit = victim.getName();
            if (plugin.getUBDatabase().maxWarns(idoit) != null && plugin.getUBDatabase().maxWarns(idoit).size() >= max) {
                String cmd = config.getString("MaxWarning.Result", "ban");
                String r = config.getString("MaxWarning.Reason", "Max Warns");
                boolean s = config.getBoolean("MaxWarning.Silent", true);
                StringBuilder sb = new StringBuilder();
                if (cmd.equalsIgnoreCase("ban")
                        || cmd.equalsIgnoreCase("kick")
                        || cmd.equalsIgnoreCase("ipban")
                        || cmd.equalsIgnoreCase("jail")
                        || cmd.equalsIgnoreCase("permaban")) {
                    sb.append(cmd).append(" ").append(idoit).append(" ");
                    if (s)
                        sb.append("-s ");
                    sb.append(r);
                    plugin.getServer().dispatchCommand(sender, sb.toString());

                } else if (cmd.equalsIgnoreCase("tempban")
                        || cmd.equalsIgnoreCase("tempipban")
                        || cmd.equalsIgnoreCase("tempjail")) {
                    sb.append(cmd).append(" ").append(idoit).append(" ");
                    if (s)
                        sb.append("-s ");
                    sb.append(config.getString("MaxWarning.Temp.Amt", "5")).append(" ").append(config.getString("MaxWarning.Temp.Mode", "day")).append(" ")
                            .append(r);
                    plugin.getServer().dispatchCommand(sender, sb.toString());
                } else {
                    String fakecmd = "ban" + " " + idoit + " " + "-s" + " " + r;
                    plugin.getServer().dispatchCommand(sender, fakecmd);
                }
                return null;
            }
        }
        plugin.getAPI().warnPlayer(name, reason, admin);
        String bcmsg = Formatting.replaceAmpersand(plugin.getString(Language.WARN_MSGTOBROADCAST));
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
            plugin.log(bcmsg);
        }
        return null;
    }
}
