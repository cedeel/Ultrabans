/* COPYRIGHT (c) 2012 Joshua McCurry
 * This work is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License
 * and use of this software or its code is an agreement to this license.
 * A full copy of this license can be found at
 * http://creativecommons.org/licenses/by-nc-sa/3.0/. 
 */
package com.modcrafting.ultrabans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import com.modcrafting.ultrabans.UltraBan;

public class Perma implements CommandExecutor{
	UltraBan plugin;
	public Perma(UltraBan ultraBan) {
		this.plugin = ultraBan;
	}
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	YamlConfiguration config = (YamlConfiguration) plugin.getConfig();
		boolean broadcast = true;
		Player player = null;
		String admin = config.getString("defAdminName", "server");
		String reason = config.getString("defReason", "not sure");
		if (sender instanceof Player){
			player = (Player)sender;
			admin = player.getName();
		}
		if(!sender.hasPermission(command.getPermission())){
			sender.sendMessage(ChatColor.RED + "You do not have the required permissions.");
			return true;
		}
		if (args.length < 1) return false;
		String p = args[0];
		
		if(plugin.autoComplete) p = plugin.util.expandName(p);
		Player victim = plugin.getServer().getPlayer(p);
		
		if(args.length > 1){
			if(args[1].equalsIgnoreCase("-s")){
				broadcast = false;
				reason = plugin.util.combineSplit(2, args, " ");
			}else{
				if(args[1].equalsIgnoreCase("-a")){
					admin = config.getString("defAdminName", "server");
					reason = plugin.util.combineSplit(2, args, " ");
				}else{
					reason = plugin.util.combineSplit(1, args, " ");
				}
			}
		}

		if(plugin.bannedPlayers.contains(p.toLowerCase())){
			String adminMsg = config.getString("messages.banMsgFailed", "&8Player &4%victim% &8is already banned!");
				if(victim == null){
					adminMsg = adminMsg.replaceAll(plugin.regexVictim, p);
				}else{
					adminMsg = adminMsg.replaceAll(plugin.regexVictim, victim.getName());
				}
			sender.sendMessage(plugin.util.formatMessage(adminMsg));
			return true;
		}

		if(victim == null){
			victim = plugin.getServer().getOfflinePlayer(p).getPlayer();
			if(victim == null){
				sender.sendMessage(ChatColor.RED + "Unable to find player!");
				return true;
			}
			
		}
		if(victim.getName() == admin){
			sender.sendMessage(ChatColor.RED + "You cannot permaban yourself!");
			return true;
		}
		if(victim.hasPermission( "ultraban.override.permaban")){
			sender.sendMessage(ChatColor.RED + "Your permaban has been denied! Player Notified!");
			victim.sendMessage(ChatColor.RED + "Player: " + admin + " Attempted to permaban you!");
			return true;
		}
				
		plugin.bannedPlayers.add(victim.getName().toLowerCase());
		plugin.db.addPlayer(victim.getName(), reason, admin, 0, 9);
		

		String adminMsg = config.getString("messages.banMsgVictim", "You have been permabanned by %admin%. Reason: %reason%");
		if(adminMsg.contains(plugin.regexAdmin)) adminMsg = adminMsg.replaceAll(plugin.regexAdmin, admin);
		if(adminMsg.contains(plugin.regexReason)) adminMsg = adminMsg.replaceAll(plugin.regexReason, reason);
		victim.kickPlayer(plugin.util.formatMessage(adminMsg));

		if(config.getBoolean("CleanOnBan")) plugin.data.deletePlyrdat(victim.getName());
		String permbanMsgBroadcast = config.getString("messages.permbanMsgBroadcast", "%victim% has been permabanned by %admin%. Reason: %reason%");
		if(permbanMsgBroadcast.contains(plugin.regexAdmin)) permbanMsgBroadcast = permbanMsgBroadcast.replaceAll(plugin.regexAdmin, admin);
		if(permbanMsgBroadcast.contains(plugin.regexReason)) permbanMsgBroadcast = permbanMsgBroadcast.replaceAll(plugin.regexReason, reason);
		if(permbanMsgBroadcast.contains(plugin.regexVictim)) permbanMsgBroadcast = permbanMsgBroadcast.replaceAll(plugin.regexVictim, victim.getName());
		if(permbanMsgBroadcast != null){
			if(broadcast){
				plugin.getServer().broadcastMessage(plugin.util.formatMessage(permbanMsgBroadcast));
			}else{
				sender.sendMessage(ChatColor.ITALIC + "Silent: " + plugin.util.formatMessage(permbanMsgBroadcast));
			}
		}
		plugin.getLogger().info(admin + " permabanned player " + victim.getName() + ".");
		return true;
	}
}
