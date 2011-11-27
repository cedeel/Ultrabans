package com.modcrafting.ultrabans.commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.modcrafting.ultrabans.UltraBan;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Import {
	public static final Logger log = Logger.getLogger("Minecraft");
	UltraBan plugin;

	public Import(UltraBan ultraBan) {
	this.plugin = ultraBan;
	}
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
    	boolean auth = false;
		Player player = null;
		String admin = "server";
		if (sender instanceof Player){
			player = (Player)sender;
			if (Permissions.Security.permission(player, "ultraban.import")){
				auth = true;
			}else{
			 if (player.isOp()) auth = true;
			}
			admin = player.getName();
		}else{
			auth = true;
		}
		if (!auth){
			sender.sendMessage(ChatColor.RED + "You do not have the required permissions.");
			return true;
		}
		if(auth){
		try {
			BufferedReader banlist = new BufferedReader(new FileReader("banned-players.txt"));
			String p;
			
			while ((p = banlist.readLine()) != null){
					if(!plugin.bannedPlayers.contains(p.toLowerCase()))
						plugin.db.addPlayer(p.toLowerCase(), "imported", admin, 0, 0);
						Bukkit.getOfflinePlayer(p).setBanned(true);
				  }
			BufferedReader bannedIP = new BufferedReader(new FileReader("banned-ips.txt"));
			String ip;
			
			while ((ip = bannedIP.readLine()) != null){
				  // add it to the database :(
					if(!plugin.bannedIPs.contains(ip))
						plugin.bannedIPs.add(ip);
					String cknullIP = plugin.db.getName(ip);
					if (cknullIP != null){
						plugin.db.addPlayer(plugin.db.getName(ip), "imported", admin, 0, 1);
					}else{
						plugin.db.setAddress("import", ip);
						plugin.db.addPlayer("import", "imported", admin, 0, 1);
					}
					Bukkit.banIP(p);
				  }
			
			sender.sendMessage(ChatColor.GREEN + "Banlist imported.");
			UltraBan.log.log(Level.INFO, admin + " imported the banlist to the database.");
			return true;
			
		} catch (IOException e) {
			UltraBan.log.log(Level.SEVERE, "[Ultrabans] could not import ban list.");
			sender.sendMessage(ChatColor.RED + "Could not import ban list.");
		}
		

		return true;
		}
		return false;
	}
}
