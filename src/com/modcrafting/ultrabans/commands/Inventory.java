package com.modcrafting.ultrabans.commands;

import com.modcrafting.ultrabans.Language;
import com.modcrafting.ultrabans.util.Formatting;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.modcrafting.ultrabans.Ultrabans;

public class Inventory extends CommandHandler {
    public Inventory(Ultrabans instance) {
        super(instance);
    }

    public String command(CommandSender sender, Command command, String[] args) {
        if (sender instanceof Player) {
            if (args.length < 1)
                return plugin.getString(Language.INVOF_ARGUMENTS);
            OfflinePlayer victim = plugin.getServer().getOfflinePlayer(args[0]);
            if (victim == null || !victim.isOnline()) {
                return Formatting.replaceAmpersand(plugin.getString(Language.INVOF_FAILED));
            }
            ((Player) sender).openInventory(victim.getPlayer().getInventory());
            return null;
        }
        return Formatting.replaceAmpersand(plugin.getString(Language.INVOF_CONSOLE));
    }
}
