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

public class Tempjail implements CommandExecutor{
	Ultrabans plugin;
	public Tempjail(Ultrabans ultraBan) {
		this.plugin = ultraBan;
	}
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	YamlConfiguration config = (YamlConfiguration) plugin.getConfig();
		boolean broadcast = true;
		Player player = null;
		String admin = plugin.admin;
		String reason = plugin.reason;
		if (sender instanceof Player){
			player = (Player)sender;
			admin = player.getName();
		}
		if(!sender.hasPermission(command.getPermission())){
			sender.sendMessage(ChatColor.RED+plugin.perms);
			return true;
		}
		if (args.length < 3) return false;

		String p = args[0];
		p = plugin.util.expandName(p);
		Player victim = plugin.getServer().getPlayer(p);
		long tempTime = 0;
		String amt="";
		String mode="";
		if(args.length > 3){
			if(args[1].equalsIgnoreCase("-s")){
				broadcast = false;
				amt=args[2];
				mode=args[3];
				reason = plugin.util.combineSplit(4, args, " ");
				tempTime = plugin.util.parseTimeSpec(amt,mode);
			}else if(args[1].equalsIgnoreCase("-a")){
				admin = plugin.admin;
				amt=args[2];
				mode=args[3];
				reason = plugin.util.combineSplit(4, args, " ");
				tempTime = plugin.util.parseTimeSpec(amt,mode);
			}else{
				amt=args[1];
				mode=args[2];
				tempTime = plugin.util.parseTimeSpec(amt,mode);
				reason = plugin.util.combineSplit(3, args, " ");
			}
		}
		if(tempTime == 0) return false;
		long temp = System.currentTimeMillis()/1000+tempTime;
		if(victim != null){
			if(victim.getName().equalsIgnoreCase(admin)){
				String bcmsg = config.getString("Messages.TempJail.Emo","You cannot tempjail yourself!");
				bcmsg = plugin.util.formatMessage(bcmsg);
				sender.sendMessage(bcmsg);
				return true;
			}
			if(victim.hasPermission("ultraban.override.tempjail")&&!admin.equalsIgnoreCase(plugin.admin)){
				String bcmsg = config.getString("Messages.TempJail.Denied","Your tempjail has been denied!");
				bcmsg = plugin.util.formatMessage(bcmsg);
				sender.sendMessage(bcmsg);
				return true;
			}
			if(plugin.jailed.contains(victim.getName().toLowerCase())){
				String failed = config.getString("Messages.TempJail.Failed", "%victim% is already jailed!");
				if(failed.contains(plugin.regexVictim)) failed = failed.replaceAll(plugin.regexVictim, p);
				failed = plugin.util.formatMessage(failed);
				sender.sendMessage(failed);
				return true;
			}
			String msgvic = config.getString("Messages.TempJail.MsgToVictim", "You have been tempjailed by %admin% for %amt% %mode%s. Reason: %reason%!");
			if(msgvic.contains(plugin.regexAdmin)) msgvic = msgvic.replaceAll(plugin.regexAdmin, admin);
			if(msgvic.contains(plugin.regexReason)) msgvic = msgvic.replaceAll(plugin.regexReason, reason);
			if(msgvic.contains(plugin.regexAmt)) msgvic = msgvic.replaceAll(plugin.regexAmt, amt);
			if(msgvic.contains(plugin.regexMode)) msgvic = msgvic.replaceAll(plugin.regexMode, mode);
			msgvic=plugin.util.formatMessage(msgvic);
			victim.sendMessage(msgvic);
			victim.teleport(plugin.jail.getJail("jail"));
			
			String bcmsg = config.getString("Messages.TempJail.MsgToBroadcast", "%victim% was tempjailed by %admin% for %amt% %mode%s. Reason: %reason%!");
			if(bcmsg.contains(plugin.regexAdmin)) bcmsg = bcmsg.replaceAll(plugin.regexAdmin, admin);
			if(bcmsg.contains(plugin.regexReason)) bcmsg = bcmsg.replaceAll(plugin.regexReason, reason);
			if(bcmsg.contains(plugin.regexVictim)) bcmsg = bcmsg.replaceAll(plugin.regexVictim, p);
			if(bcmsg.contains(plugin.regexAmt)) bcmsg = bcmsg.replaceAll(plugin.regexAmt, amt);
			if(bcmsg.contains(plugin.regexMode)) bcmsg = bcmsg.replaceAll(plugin.regexMode, mode);
			bcmsg = plugin.util.formatMessage(bcmsg);
			if(bcmsg != null){
				if(broadcast){
					plugin.getServer().broadcastMessage(bcmsg);
				}else{
					sender.sendMessage(ChatColor.ITALIC+"Silent: "+bcmsg);
				}
			}
			plugin.tempJail.put(victim.getName().toLowerCase(), temp);
			plugin.db.addPlayer(victim.getName(), reason, admin, temp, 6);
			plugin.jailed.add(p.toLowerCase());
			plugin.getLogger().info(bcmsg);
			return true;
		}else{
			victim = plugin.getServer().getOfflinePlayer(p).getPlayer();
			if(victim != null){
				if(victim.hasPermission("ultraban.override.tempjail")&&!admin.equalsIgnoreCase(plugin.admin)){
					String bcmsg = config.getString("Messages.TempJail.Denied","Your tempjail has been denied!");
					bcmsg = plugin.util.formatMessage(bcmsg);
					sender.sendMessage(bcmsg);
					return true;
				}
			}
			if(plugin.jailed.contains(p.toLowerCase())){
				String bcmsg = config.getString("Messages.TempJail.Failed","%victim% is already jailed!");
				if(bcmsg.contains(plugin.regexVictim)) bcmsg = bcmsg.replaceAll(plugin.regexVictim, p);
				bcmsg = plugin.util.formatMessage(bcmsg);
				sender.sendMessage(bcmsg);
				return true;
			}
			String bcmsg = config.getString("Messages.TempJail.MsgToBroadcast", "%victim% was tempjailed by %admin% for %amt% %mode%s. Reason: %reason%!");
			if(bcmsg.contains(plugin.regexAdmin)) bcmsg = bcmsg.replaceAll(plugin.regexAdmin, admin);
			if(bcmsg.contains(plugin.regexReason)) bcmsg = bcmsg.replaceAll(plugin.regexReason, reason);
			if(bcmsg.contains(plugin.regexVictim)) bcmsg = bcmsg.replaceAll(plugin.regexVictim, p);
			if(bcmsg.contains(plugin.regexAmt)) bcmsg = bcmsg.replaceAll(plugin.regexAmt, amt);
			if(bcmsg.contains(plugin.regexMode)) bcmsg = bcmsg.replaceAll(plugin.regexMode, mode);
			bcmsg = plugin.util.formatMessage(bcmsg);
			if(broadcast){
				plugin.getServer().broadcastMessage(bcmsg);
			}else{
				sender.sendMessage(ChatColor.ITALIC + "Silent: " + bcmsg);
			}
			plugin.tempJail.put(p.toLowerCase(), temp);
			plugin.jailed.add(p.toLowerCase());
			plugin.db.addPlayer(p, reason, admin, temp, 6);
			plugin.getLogger().info(bcmsg);
			return true;
		}
	}
}
