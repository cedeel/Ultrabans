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

import java.util.Date;
import java.util.List;

import com.modcrafting.ultrabans.Language;
import com.modcrafting.ultrabans.util.Formatting;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.modcrafting.ultrabans.Ultrabans;
import com.modcrafting.ultrabans.util.BanInfo;
import com.modcrafting.ultrabans.util.BanType;

public class History extends CommandHandler {
    public History(Ultrabans instance) {
        super(instance);
    }

    public String command(CommandSender sender, Command command, String[] args) {
        int size = 5;
        if (args.length > 0)
            try {
                size = Integer.parseInt(args[0].trim());
            } catch (NumberFormatException ignored) {}
        List<BanInfo> bans = plugin.getUBDatabase().listRecent(size);
        if (bans.size() < 1)
            return plugin.getString(Language.HISTORY_FAILED);
        String msg = plugin.getString(Language.HISTORY_HEADER);
        if (msg.contains(Formatting.AMOUNT))
            msg = msg.replace(Formatting.AMOUNT, args[0]);
        sender.sendMessage(Formatting.replaceAmpersand(msg));
        for (BanInfo ban : bans) {
            Date date = new Date();
            date.setTime(ban.getEndTime() * 1000);
            String dateStr = date.toString();
            switch (ban.getType()) {
                case TEMPBAN:
                case TEMPIPBAN:
                case TEMPJAIL: {
                    sender.sendMessage(ChatColor.RED + BanType.toCode(ban.getType()) + ": " + ban.getName() + ChatColor.GRAY + " by " + ban.getAdmin() + " till " + dateStr.substring(4, 19) + " for " + ban.getReason());
                    break;
                }
                default: {
                    sender.sendMessage(ChatColor.RED + BanType.toCode(ban.getType()) + ": " + ban.getName() + ChatColor.GRAY + " by " + ban.getAdmin() + " for " + ban.getReason());
                    break;
                }
            }
        }
        return null;
    }
}