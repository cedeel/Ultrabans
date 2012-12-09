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

import com.modcrafting.ultrabans.Ultrabans;

public class Mute implements CommandExecutor {
	Ultrabans plugin;
	public Mute(Ultrabans ultraBan) {
		this.plugin = ultraBan;
	}
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.hasPermission(command.getPermission())){
			sender.sendMessage(ChatColor.RED+plugin.perms);
			return true;
		}
    	YamlConfiguration config = (YamlConfiguration) plugin.getConfig();
		Player player = null;
		String admin = plugin.admin;
		if (sender instanceof Player){
			player = (Player)sender;
			admin = player.getName();
		}
		if (args.length < 1) return false;
		String p = plugin.util.expandName(args[0]); 
		Player victim = plugin.getServer().getPlayer(p);
		if(victim != null){
			if(victim.getName().equalsIgnoreCase(admin)){
				String bcmsg = config.getString("Messages.Mute.Emo","You cannot mute yourself!");
				bcmsg = plugin.util.formatMessage(bcmsg);
				sender.sendMessage(bcmsg);
				return true;
			}
			if(victim.hasPermission("ultraban.override.mute")&&!admin.equalsIgnoreCase(plugin.admin)){
				String bcmsg = config.getString("Messages.Mute.Denied","Your mute has been denied!");
				bcmsg = plugin.util.formatMessage(bcmsg);
				sender.sendMessage(bcmsg);
				return true;
			}
			if (plugin.muted.contains(p.toLowerCase())){
				plugin.muted.remove(p.toLowerCase());
		 		victim.sendMessage(plugin.util.formatMessage(config.getString("Messages.Mute.UnmuteMsgToVictim","You have been unmuted.")));
				String adminMsgs = config.getString("Messages.Mute.UnmuteMsgToSender","You have unmuted %victim%.");
				if(adminMsgs.contains(plugin.regexVictim)) adminMsgs = adminMsgs.replaceAll(plugin.regexVictim, p);
		 		sender.sendMessage(plugin.util.formatMessage(adminMsgs));
				return true;
			}
			plugin.muted.add(p.toLowerCase());
	 		victim.sendMessage(plugin.util.formatMessage(config.getString("Messages.Mute.MuteMsgToVictim","You have been muted!")));
	 		String adminMsgs = config.getString("Messages.Mute.MuteMsgToSender","You have muted %victim%.");
			if(adminMsgs.contains(plugin.regexVictim)) adminMsgs = adminMsgs.replaceAll(plugin.regexVictim, p);
			adminMsgs=plugin.util.formatMessage(adminMsgs);
	 		sender.sendMessage(adminMsgs);
			plugin.db.addPlayer(p, "Muted", admin, 0, 7);
			plugin.getLogger().info(admin + " muted player " + p + ".");
		}else{
			if (plugin.muted.contains(p.toLowerCase())){
				plugin.muted.remove(p.toLowerCase());
				String adminMsgs = config.getString("Messages.Mute.UnmuteMsgToSender","You have unmuted %victim%.");
				if(adminMsgs.contains(plugin.regexVictim)) adminMsgs = adminMsgs.replaceAll(plugin.regexVictim, p);
		 		sender.sendMessage(plugin.util.formatMessage(adminMsgs));
			}else{
				sender.sendMessage(ChatColor.RED + "Player must be online!");				
			}
		}	
		return true;	
	}

}
