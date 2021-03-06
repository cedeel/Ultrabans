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

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.modcrafting.ultrabans.Language;
import com.modcrafting.ultrabans.util.Formatting;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.modcrafting.ultrabans.Ultrabans;

public class CheckIP extends CommandHandler {
    public CheckIP(Ultrabans ultraBan) {
        super(ultraBan);
    }

    public String command(final CommandSender sender, Command command, final String[] args) {
        if (args.length < 1)
            return plugin.getString(Language.CHECKIP_ARGUMENTS);
        try {
            String p = args[0];
            String ip = plugin.getUBDatabase().getAddress(p);
            InetAddress inet;
            if (ip != null) {
                inet = InetAddress.getByName(ip);
            } else {
                OfflinePlayer n = plugin.getServer().getOfflinePlayer(p);
                if (n != null && n.isOnline()) {
                    inet = n.getPlayer().getAddress().getAddress();
                    ip = inet.getHostAddress();
                    plugin.getUBDatabase().setAddress(n.getName().toLowerCase(), ip);
                } else {
                    return plugin.getString(Language.CHECKIP_NOPLAYER);
                }
            }
            sender.sendMessage(ChatColor.YELLOW + "------[" + p + "]------");
            sender.sendMessage(Formatting.replaceAmpersand(plugin.getString(Language.CHECKIP_MSG1))
                    + ChatColor.GRAY + ip);
            sender.sendMessage(Formatting.replaceAmpersand(plugin.getString(Language.CHECKIP_MSG2))
                    + ChatColor.GRAY + inet.getHostName());
        } catch (UnknownHostException e) {
            sender.sendMessage(Formatting.replaceAmpersand(plugin.getString(Language.CHECKIP_EXCEPTION)));
        }

        return null;
    }
}
