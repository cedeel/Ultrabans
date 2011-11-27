package com.modcrafting.ultrabans.commands;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.modcrafting.ultrabans.UltraBan;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Export implements CommandExecutor{
	public static final Logger log = Logger.getLogger("Minecraft");
	UltraBan plugin;
	
	public Export(UltraBan ultraBan) {
		this.plugin = ultraBan;
	}
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		boolean auth = false;
		Player player = null;
		if (sender instanceof Player){
			player = (Player)sender;
			if (Permissions.Security.permission(player, "ultraban.export"))  auth = true;
		}else{
			auth = true;
		}
		
		if (auth) {
			try
			{
				BufferedWriter banlist = new BufferedWriter(new FileWriter("banned-players.txt",true));
				for(String p : plugin.bannedPlayers){
					banlist.newLine();
					banlist.write(p);
				}
				banlist.close();
				BufferedWriter iplist = new BufferedWriter(new FileWriter("banned-ips.txt",true));
				for(String p : plugin.bannedIPs){
					iplist.newLine();
					iplist.write(p);
				}
				iplist.close();
			}
			catch(IOException e)          
			{
				UltraBan.log.log(Level.SEVERE,"UltraBan: Couldn't write to banned-players.txt");
			}
			sender.sendMessage("�2Exported banlist to banned-players.txt.");
			sender.sendMessage("�2Exported iplist to banned-ips.txt.");
			return true;
		}else{
			sender.sendMessage(ChatColor.RED + "You do not have the required permissions.");
			return true;
		}
	}

}
