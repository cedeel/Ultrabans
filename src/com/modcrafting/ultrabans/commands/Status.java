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
import com.modcrafting.ultrabans.util.Formatting;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.modcrafting.ultrabans.Ultrabans;

public class Status extends CommandHandler {
    public Status(Ultrabans instance) {
        super(instance);
    }

    public String command(final CommandSender sender, Command command, String[] args) {
        sender.sendMessage(Formatting.replaceAmpersand(plugin.getString(Language.STATUS_CACHEHEADER)));
        sender.sendMessage(Formatting.replaceAmpersand(plugin.getString(Language.STATUS_CACHEBANS)
                .replace(Formatting.AMOUNT, String.valueOf(plugin.cache.size()))));
        sender.sendMessage(Formatting.replaceAmpersand(plugin.getString(Language.STATUS_CACHEIPBANS)
                .replace(Formatting.AMOUNT, String.valueOf(plugin.cacheIP.size()))));
        int counter = 0;
        counter = counter + plugin.cache.toString().getBytes().length;
        counter = counter + plugin.cacheIP.toString().getBytes().length;
        return plugin.getString(Language.STATUS_USAGE).replace(Formatting.AMOUNT, String.valueOf(counter));
    }
}
