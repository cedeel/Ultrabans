/* COPYRIGHT (c) 2012 Joshua McCurry
 * This work is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License
 * and use of this software or its code is an agreement to this license.
 * A full copy of this license can be found at
 * http://creativecommons.org/licenses/by-nc-sa/3.0/. 
 */

package com.modcrafting.ultrabans.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.modcrafting.ultrabans.UltraBan;

public class CheckIP implements CommandExecutor{
	String permission = "ultraban.checkip";
	UltraBan plugin;
	public CheckIP(UltraBan ultraBan) {
		this.plugin = ultraBan;
	}
	public boolean onCommand(final CommandSender sender, Command command, String commandLabel, final String[] args) {
		final YamlConfiguration config = (YamlConfiguration) plugin.getConfig();
		boolean auth = false;
		Player player = null;
		if (sender instanceof Player){
			player = (Player)sender;
			if(player.hasPermission(permission) || player.isOp()) auth = true;
		}else{
			auth = true; //if sender is not a player - Console
		}
		if (!auth){
			sender.sendMessage(ChatColor.RED + "You do not have the required permissions.");
			return true;
		}
		if (args.length < 1) return false;
		plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin,new Runnable(){

			@Override
			public void run() {
		String p = args[0];
		String ip = plugin.db.getAddress(p.toLowerCase());
		InetAddress InetP;
		if(ip == null){
			plugin.db.setAddress(plugin.getServer().getPlayer(p).getName().toLowerCase(), plugin.getServer().getPlayer(p).getAddress().getAddress().getHostAddress());
		}
		try {
			InetP = InetAddress.getByName(ip);
			sender.sendMessage(ChatColor.YELLOW + "IP Address: " + ip);
			sender.sendMessage(ChatColor.YELLOW + "Host Address: " + InetP.getHostAddress());
			sender.sendMessage(ChatColor.YELLOW + "Host Name: " + InetP.getHostName());
			sender.sendMessage(ChatColor.YELLOW + "Connection: " + InetP.getCanonicalHostName());
			
			try {
				boolean ping = InetP.isReachable(config.getInt("HostTimeOut", 1800));
				if (ping){
					sender.sendMessage(ChatColor.GREEN + "Ping Test Passed.");
				}else{
					sender.sendMessage(ChatColor.RED + "Ping Test Failed.");
				}
			} catch (IOException e) {
				sender.sendMessage(ChatColor.RED + "Ping Test Failed.");
			}
		} catch (UnknownHostException e) {
			sender.sendMessage(ChatColor.RED + "Gathering Information Failed: Recommend Kick!");
		}
			}
		});
		return true;
		
	}
}
