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

public class Tempipban extends CommandHandler {
    public Tempipban(Ultrabans instance) {
        super(instance);
    }

    public String command(CommandSender sender, Command command, String[] args) {
        if (args.length < 3)
            return plugin.getString(Language.TEMPIPBAN_ARGUMENTS);
        boolean broadcast = true;
        String admin = Ultrabans.DEFAULT_ADMIN;
        String reason = Ultrabans.DEFAULT_REASON;
        if (sender instanceof Player)
            admin = sender.getName();
        String name = Formatting.expandName(args[0]);
        if (name.equalsIgnoreCase(admin))
            return plugin.getString(Language.TEMPIPBAN_EMO);
        long tempTime = 0;
        String amt = "";
        String mode = "";
        if (args.length >= 4) {
            if (args[1].charAt(0) == '-') {
                if (args[1].equalsIgnoreCase("-s")
                        && sender.hasPermission(command.getPermission() + ".silent")) {
                    broadcast = false;
                }
                if (args[1].equalsIgnoreCase("-a")
                        && sender.hasPermission(command.getPermission() + ".anon")) {
                    admin = Ultrabans.DEFAULT_ADMIN;
                }
                amt = args[2];
                mode = args[3];
                reason = Formatting.combineSplit(4, args);
            } else {
                amt = args[1];
                mode = args[2];
                reason = Formatting.combineSplit(3, args);
            }

            tempTime = Formatting.parseTimeSpec(amt, mode);
        } else if (args.length == 3) {
            amt = args[1];
            mode = args[2];
            tempTime = Formatting.parseTimeSpec(amt, mode);
            reason = Formatting.combineSplit(3, args);
        }
        if (tempTime == 0)
            return plugin.getString(Language.TEMPBAN_TIMEFAIL);
        long temp = System.currentTimeMillis() / 1000 + tempTime;
        OfflinePlayer victim = plugin.getServer().getOfflinePlayer(name);
        if (victim != null) {
            if (victim.isOnline()) {
                if (victim.getPlayer().hasPermission("ultrabans.override.tempban") &&
                        !admin.equalsIgnoreCase(Formatting.ADMIN))
                    return plugin.getString(Language.TEMPIPBAN_DENIED);
                String msgvic = plugin.getString(Language.TEMPIPBAN_MSGTOVICTIM);
                if (msgvic.contains(Formatting.ADMIN))
                    msgvic = msgvic.replace(Formatting.ADMIN, admin);
                if (msgvic.contains(Formatting.REASON))
                    msgvic = msgvic.replace(Formatting.REASON, reason);
                if (msgvic.contains(Formatting.AMOUNT))
                    msgvic = msgvic.replace(Formatting.AMOUNT, amt);
                if (msgvic.contains(Formatting.MODE))
                    msgvic = msgvic.replace(Formatting.MODE, mode);
                victim.getPlayer().kickPlayer(Formatting.replaceAmpersand(msgvic));
            }
            name = victim.getName();
        }
        String offlineip = plugin.getUBDatabase().getAddress(name);
        if (offlineip != null) {
            if (plugin.cacheIP.containsKey(offlineip)) {
                for (BanInfo info : plugin.cache.get(offlineip)) {
                    if (info.getType() == BanType.TEMPBAN
                            || info.getType() == BanType.BAN) {
                        String failed = plugin.getString(Language.TEMPIPBAN_FAILED);
                        if (failed.contains(Formatting.VICTIM))
                            failed = failed.replace(Formatting.VICTIM, name);
                        return failed;
                    }
                }
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("tempban ").append(name).append(" ");
            if (!broadcast)
                sb.append("-s ");
            sb.append(amt).append(" ").append(mode).append(" ").append(reason);
            plugin.getServer().dispatchCommand(sender, sb.toString());
            String failed = plugin.getString(Language.TEMPIPBAN_IPNOTFOUND);
            if (failed.contains(Formatting.VICTIM))
                failed = failed.replace(Formatting.VICTIM, name);
            return failed;
        }

        plugin.getAPI().tempipbanPlayer(name, offlineip, reason, temp, admin);
        String bcmsg = plugin.getString(Language.TEMPIPBAN_MSGTOBROADCAST);
        if (bcmsg.contains(Formatting.ADMIN))
            bcmsg = bcmsg.replace(Formatting.ADMIN, admin);
        if (bcmsg.contains(Formatting.REASON))
            bcmsg = bcmsg.replace(Formatting.REASON, reason);
        if (bcmsg.contains(Formatting.VICTIM))
            bcmsg = bcmsg.replace(Formatting.VICTIM, name);
        if (bcmsg.contains(Formatting.AMOUNT))
            bcmsg = bcmsg.replace(Formatting.AMOUNT, amt);
        if (bcmsg.contains(Formatting.MODE))
            bcmsg = bcmsg.replace(Formatting.MODE, mode);
        if (broadcast) {
            plugin.getServer().broadcastMessage(bcmsg);
        } else {
            sender.sendMessage(ChatColor.ITALIC + "Silent: " + bcmsg);
            plugin.log(bcmsg);
        }
        return bcmsg;
    }
}
