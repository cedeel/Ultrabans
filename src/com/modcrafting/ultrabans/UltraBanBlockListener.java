package com.modcrafting.ultrabans;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class UltraBanBlockListener extends BlockListener {
	public static UltraBan plugin;
	public UltraBanBlockListener(UltraBan instance) {
	plugin = instance;
	}
	public void onBlockPlace(BlockPlaceEvent event){
		YamlConfiguration config = (YamlConfiguration) plugin.getConfig();
		 Player player = event.getPlayer();
		 if(plugin.jailed.contains(player.getName().toLowerCase())){
			String adminMsg = config.getString("messages.jailPlaceMsg", "You cannot place blocks while you are jailed!");
		 player.sendMessage(ChatColor.GRAY + adminMsg);
		 event.setCancelled(true);
		 }
	 }
	public void onBlockBreak(BlockBreakEvent event){
		YamlConfiguration config = (YamlConfiguration) plugin.getConfig();
		 Player player = event.getPlayer();
		 if(plugin.jailed.contains(player.getName().toLowerCase())){
			String adminMsg = config.getString("messages.jailBreakMsg", "You cannot break blocks while you are jailed!");
		 player.sendMessage(ChatColor.GRAY + adminMsg);
		 event.setCancelled(true);
		 }
	}
		 
}
