package com.modcrafting.ultrabans.commands;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.modcrafting.ultrabans.UltraBan;

public class Tempipban implements CommandExecutor{
	public static final Logger log = Logger.getLogger("Minecraft");
	UltraBan plugin;
	String permission = "ultraban.tempipban";
	public Tempipban(UltraBan ultraBan) {
		this.plugin = ultraBan;
	}
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		YamlConfiguration config = (YamlConfiguration) plugin.getConfig();
		boolean auth = false;
		Player player = null;
		boolean broadcast = true;
		String reason = config.getString("defReason", "not sure");
		String admin = config.getString("defAdminName", "server");
		if (sender instanceof Player){
			player = (Player)sender;
			if(player.hasPermission(permission) || player.isOp()) auth = true;
			admin = player.getName();
		}else{
			auth = true; //if sender is not a player - Console
		}
		if (!auth){
			sender.sendMessage(ChatColor.RED + "You do not have the required permissions.");
			return true;
		}
		if (args.length < 3) return false;

		String p = args[0]; // Get the victim's potential name
		
		if(plugin.autoComplete)
			p = plugin.util.expandName(p);
		Player victim = plugin.getServer().getPlayer(p);
		
		long tempTime = 0;
		if(args.length > 3){
			if(args[1].equalsIgnoreCase("-s")){
				broadcast = false;
				reason = plugin.util.combineSplit(4, args, " ");
				tempTime = plugin.util.parseTimeSpec(args[2],args[3]);
			}else if(args[1].equalsIgnoreCase("-a")){
				admin = config.getString("defAdminName", "server");
				reason = plugin.util.combineSplit(4, args, " ");
				tempTime = plugin.util.parseTimeSpec(args[2],args[3]);
			}else{
				tempTime = plugin.util.parseTimeSpec(args[1],args[2]);
				reason = plugin.util.combineSplit(3, args, " ");
				
			}
		}
		if(tempTime == 0) return false;
		
		long temp = System.currentTimeMillis()/1000+tempTime;
		
		if(victim != null){
			if(victim.getName() == admin){
				sender.sendMessage(ChatColor.RED + "You cannot tempipban yourself!");
				return true;
			}
			if(victim.hasPermission( "ultraban.override.tempipban")){
				sender.sendMessage(ChatColor.RED + "Your tempipban has been denied! Player Notified!");
				victim.sendMessage(ChatColor.RED + "Player:" + admin + " Attempted to tempipban you!");
				return true;
			}	
			if(plugin.bannedPlayers.contains(victim.getName().toLowerCase())){
				sender.sendMessage(ChatColor.BLUE + victim.getName() +  ChatColor.GRAY + " is already banned for " + reason);
				return true;
			}
			String offlineip = plugin.db.getAddress(p.toLowerCase());
			if(offlineip != null){
				plugin.bannedIPs.add(offlineip);
			}else{
				sender.sendMessage(ChatColor.GRAY + "IP address not found by Ultrabans for " + p);
				sender.sendMessage(ChatColor.GRAY + "Processed as a normal tempban for " + p);
				plugin.tempBans.put(victim.getName().toLowerCase(), temp);
				plugin.db.addPlayer(victim.getName(), reason, admin, temp, 0);
				String tempbanMsgVictim = config.getString("messages.tempbanMsgVictim", "You have been temp. banned by %admin%. Reason: %reason%!");
				tempbanMsgVictim = tempbanMsgVictim.replaceAll(plugin.regexAdmin, admin);
				tempbanMsgVictim = tempbanMsgVictim.replaceAll(plugin.regexReason, reason);
				victim.kickPlayer(plugin.util.formatMessage(tempbanMsgVictim));
				log.log(Level.INFO, "[UltraBan] " + admin + " banned player " + p + ".");
				return true;
			}
			plugin.tempBans.put(victim.getName().toLowerCase(), temp);
			plugin.db.addPlayer(victim.getName(), reason, admin, temp, 1);
			log.log(Level.INFO, "[UltraBan] " + admin + " tempipbanned player " + victim.getName() + ".");
			
			String tempbanMsgVictim = config.getString("messages.tempipbanMsgVictim", "You have been temp. banned by %admin%. Reason: %reason%!");
			tempbanMsgVictim = tempbanMsgVictim.replaceAll(plugin.regexAdmin, admin);
			tempbanMsgVictim = tempbanMsgVictim.replaceAll(plugin.regexReason, reason);
			victim.kickPlayer(plugin.util.formatMessage(tempbanMsgVictim));
			
			String tempbanMsgBroadcast = config.getString("messages.tempipbanMsgBroadcast", "%victim% was temp. banned by %admin%. Reason: %reason%!");
			tempbanMsgBroadcast = tempbanMsgBroadcast.replaceAll(plugin.regexAdmin, admin);
			tempbanMsgBroadcast = tempbanMsgBroadcast.replaceAll(plugin.regexReason, reason);
			tempbanMsgBroadcast = tempbanMsgBroadcast.replaceAll(plugin.regexVictim, victim.getName());
			if(broadcast){
				plugin.getServer().broadcastMessage(plugin.util.formatMessage(tempbanMsgBroadcast));
			}else{
				sender.sendMessage(plugin.util.formatMessage(ChatColor.ITALIC + tempbanMsgBroadcast));
			}
		}else{
			victim = plugin.getServer().getOfflinePlayer(p).getPlayer();
			if(victim != null){
				if(victim.hasPermission("ultraban.override.tempipban")){
					sender.sendMessage(ChatColor.RED + "Your tempipban has been denied!");
					return true;
				}
			}
			if(plugin.bannedPlayers.contains(p.toLowerCase())){
				sender.sendMessage(ChatColor.BLUE + p +  ChatColor.GRAY + " is already banned for " + reason);
				return true;
			}
			plugin.tempBans.put(p.toLowerCase(), temp);
			plugin.db.addPlayer(p, reason, admin, temp, 1);
			
			log.log(Level.INFO, "[UltraBan] " + admin + " tempbanned player " + p + ".");
			String tempbanMsgBroadcast = config.getString("messages.tempbanMsgBroadcast", "%victim% was temp. banned by %admin%. Reason: %reason%!");
			tempbanMsgBroadcast = tempbanMsgBroadcast.replaceAll(plugin.regexAdmin, admin);
			tempbanMsgBroadcast = tempbanMsgBroadcast.replaceAll(plugin.regexReason, reason);
			tempbanMsgBroadcast = tempbanMsgBroadcast.replaceAll(plugin.regexVictim, p);
			if(broadcast){
				plugin.getServer().broadcastMessage(plugin.util.formatMessage(tempbanMsgBroadcast));
			}else{
				sender.sendMessage(plugin.util.formatMessage(ChatColor.ITALIC + tempbanMsgBroadcast));
			}
		}
		return true;
	}
}
