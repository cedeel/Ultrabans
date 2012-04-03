package com.modcrafting.ultrabans.commands;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.modcrafting.ultrabans.UltraBan;

public class History implements CommandExecutor{
	public static final Logger log = Logger.getLogger("Minecraft");
	UltraBan plugin;
	
	public History(UltraBan ultraBan) {
		this.plugin = ultraBan;
	
	}private String banType(int num){
		switch(num){
		case 0: return "Ban  ";
		case 1: return "IPBan";
		case 2: return "Warn ";
		case 3: return "Kick ";
		case 4: return "Fine ";
		case 5: return "Unban";
		case 6: return "Jail ";
		case 7: return "Mute ";
		case 9: return "PERMA";
		default: return "?";
		}
	}
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		boolean auth = false;
		Player player = null;
		if (sender instanceof Player){
			player = (Player)sender;
			if (plugin.setupPermissions()){
				if (plugin.permission.has(player, "ultraban.history")) auth = true;
			}else{
			 if (player.isOp()) auth = true; 
			 }
		}else{
			auth = true; //if sender is not a player - Console
		}
		if (!auth){
			sender.sendMessage(ChatColor.RED + "You do not have the required permissions.");
			return true;
		}else{
		if (args.length < 1) return false;
		String p = args[0];
		
		
		List<EditBan> bans = plugin.db.listRecent(p);
		//NPE for lower bans is empty 
		if(bans.size() < 1){
			sender.sendMessage(ChatColor.GREEN + "Error in command");
			return true;
		}
		sender.sendMessage(ChatColor.BLUE + "Ultrabans Listing " + ChatColor.GRAY + args[0] + ChatColor.BLUE + " records.");
		for(EditBan ban : bans){

			Date date = new Date();
			date.setTime(ban.time*1000);
			String dateStr = date.toString();
			//DDD mmm dd hh:mm:ss zz yyyy
			
			sender.sendMessage(ChatColor.RED + banType(ban.type) + ": " + ban.name + ChatColor.GRAY + " by " + ban.admin + " on " + dateStr.substring(4, 19) + " for " + ban.reason);
		}
		return true;
		}
	}
}