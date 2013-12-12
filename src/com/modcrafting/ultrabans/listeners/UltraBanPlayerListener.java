/* COPYRIGHT (c) 2013 Deathmarine (Joshua McCurry)
 * This file is part of Ultrabans.
 * Ultrabans is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Ultrabans is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Ultrabans.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.modcrafting.ultrabans.listeners;

import com.modcrafting.ultrabans.Language;
import com.modcrafting.ultrabans.Ultrabans;
import com.modcrafting.ultrabans.util.BanInfo;
import com.modcrafting.ultrabans.util.BanType;
import com.modcrafting.ultrabans.util.Formatting;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class UltraBanPlayerListener implements Listener {
    private Ultrabans plugin;
    private String spamcheck = null;
    private int spamCount = 0;
    private FileConfiguration config;

    public UltraBanPlayerListener(Ultrabans ultraBans) {
        plugin = ultraBans;
        config = ultraBans.getConfig();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent event) {
        //TODO Set IP...
        String reason = Ultrabans.DEFAULT_REASON;
        String admin = Ultrabans.DEFAULT_ADMIN;
        final Player player = event.getPlayer();
        String ip = event.getAddress().getHostAddress();
        plugin.getUBDatabase().setAddress(player.getName().toLowerCase(), ip);
        if (plugin.cacheIP.containsKey(ip)) {
            for (BanInfo info : plugin.cacheIP.get(player.getName().toLowerCase())) {
                if (info.getType() == BanType.IPBAN || info.getType() == BanType.TEMPIPBAN) {
                    //TODO:TempCheck via Type

                    if (!admin.equals(info.getAdmin()))
                        admin = info.getAdmin();
                    if (!reason.equals(info.getReason()))
                        reason = info.getReason();
                    String bcmsg = plugin.getString(Language.IPBAN_LOGIN);
                    if (bcmsg.contains(Formatting.ADMIN))
                        bcmsg = bcmsg.replaceAll(Formatting.ADMIN, admin);
                    if (bcmsg.contains(Formatting.REASON))
                        bcmsg = bcmsg.replaceAll(Formatting.REASON, reason);
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, Formatting.replaceAmpersand(bcmsg));
                }
            }
        }

        if (plugin.cache.containsKey(player.getName().toLowerCase())) {
            for (BanInfo info : plugin.cache.get(player.getName().toLowerCase())) {
                if (info.getType() == BanType.BAN || info.getType() == BanType.TEMPBAN) {

                    //TODO:TempCheck via Type

                    if (!admin.equals(info.getAdmin()))
                        admin = info.getAdmin();
                    if (!reason.equals(info.getReason()))
                        reason = info.getReason();
                    String bcmsg = plugin.getString(Language.BAN_LOGIN);
                    if (bcmsg.contains(Formatting.ADMIN))
                        bcmsg = bcmsg.replaceAll(Formatting.ADMIN, admin);
                    if (bcmsg.contains(Formatting.REASON))
                        bcmsg = bcmsg.replaceAll(Formatting.REASON, reason);
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, Formatting.replaceAmpersand(bcmsg));
                }

            }
        }
        if (config.getBoolean("Lockdown", false) && !player.hasPermission("ultrabans.override.lockdown")) {
            String lockMsgLogin = plugin.getString(Language.LOCKDOWN_LOGINMSG);
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Formatting.replaceAmpersand(lockMsgLogin));
            plugin.getLogger().info(player.getName() + " attempted to join during lockdown.");
        }

        if (!player.hasPermission("ultrabans.override.dupeip") && config.getBoolean("Login.DupeCheck.Enable", true)) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    String ip = plugin.getUBDatabase().getAddress(player.getName());
                    if (ip != null) {
                        List<String> list = plugin.getUBDatabase().listPlayers(ip);
                        for (Player admin : plugin.getServer().getOnlinePlayers()) {
                            if (admin.hasPermission("ultrabans.dupeip")) {
                                for (String name : list) {
                                    if (!name.equalsIgnoreCase(player.getName()))
                                        admin.sendMessage(ChatColor.GRAY + "Player: " + name + " duplicates player: " + player.getName() + "!");
                                }
                            }
                        }
                    }
                }
            }, 20L);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onAsyncLogin(AsyncPlayerPreLoginEvent event) {
        String ip = event.getAddress().getHostAddress();
        String reason = Ultrabans.DEFAULT_ADMIN;
        String admin = Ultrabans.DEFAULT_REASON;
        if (plugin.cacheIP.containsKey(ip)) {
            for (BanInfo info : plugin.cacheIP.get(ip)) {
                if (info.getType() == BanType.IPBAN || info.getType() == BanType.TEMPIPBAN) {
                    //TODO:TempCheck via Type

                    if (admin.equals(info.getAdmin()))
                        admin = info.getAdmin();
                    if (reason.equals(info.getReason()))
                        reason = info.getReason();
                    String bcmsg = plugin.getString(Language.IPBAN_LOGIN);
                    if (bcmsg.contains(Formatting.ADMIN))
                        bcmsg = bcmsg.replaceAll(Formatting.ADMIN, admin);
                    if (bcmsg.contains(Formatting.REASON))
                        bcmsg = bcmsg.replaceAll(Formatting.REASON, reason);
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, Formatting.replaceAmpersand(bcmsg));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String args[] = event.getMessage().split(" ");
        String adminMsg = config.getString("Messages.Mute.Chat", "Your cry falls on deaf ears.");

        if (config.getBoolean("Jail.Vanilla", true) && plugin.cache.containsKey(player.getName().toLowerCase())) {
            List<BanInfo> list = plugin.cache.get(player.getName().toLowerCase());
            for (BanInfo info : list) {
                if (info.getType() == BanType.TEMPJAIL || info.getType() == BanType.JAIL) {
                    if (tempjailCheck(player, info) &&
                            config.getStringList("Jail.AllowedCommands").contains(args[0]))
                        return;
                    player.sendMessage(Formatting.replaceAmpersand(adminMsg));
                    event.setCancelled(true);
                }
            }
        }

        if (plugin.muted.contains(player.getName().toLowerCase()) && config.getBoolean("Muted.Vanilla", true)) {
            if (config.getStringList("Mute.AllowedCommands").contains(args[0])) return;
            player.sendMessage(Formatting.replaceAmpersand(adminMsg));
            event.setCancelled(true);
        }

    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String adminMsg = config.getString("Messages.Mute.Chat", "Your cry falls on deaf ears.");
        if (plugin.muted.contains(player.getName().toLowerCase())) {
            player.sendMessage(adminMsg);
            event.setCancelled(true);
        }
        if (plugin.cache.containsKey(player.getName().toLowerCase())
                && config.getBoolean("Jail.Mute", true)) {
            List<BanInfo> list = plugin.cache.get(player.getName().toLowerCase());
            for (BanInfo info : list) {
                if (info.getType() == BanType.TEMPJAIL || info.getType() == BanType.JAIL) {
                    if (tempjailCheck(player, info))
                        return;
                    player.sendMessage(Formatting.replaceAmpersand(adminMsg));
                    event.setCancelled(true);
                }
            }
        }
        if (config.getBoolean("Chat.IPCheck.Enable", true)) {
            ipCheck(player, message, event);
        }
        if (config.getBoolean("Chat.SpamCheck.Enable", true)) {
            spamCheck(player, message, event);
        }
        if (config.getBoolean("Chat.SwearCensor.Enable", true)) {
            swearCheck(player, message, event);
        }
    }

    private boolean tempjailCheck(Player player, BanInfo info) {
        long tempTime = info.getEndTime();
        long now = System.currentTimeMillis() / 1000;
        long diff = tempTime - now;
        if (diff <= 0) {
            List<BanInfo> list = plugin.cache.get(player.getName().toLowerCase());
            list.remove(info);
            plugin.cache.put(player.getName().toLowerCase(), list);
            plugin.getAPI().pardonPlayer(player.getName(), info.getAdmin());
            Location stlp = plugin.jail.getJail("release");
            player.teleport(stlp);
            String bcmsg = plugin.getConfig().getString("Messages.Pardon.Msg", "%victim% was released from jail by %admin%!");
            if (bcmsg.contains(Formatting.ADMIN)) bcmsg = bcmsg.replaceAll(Formatting.ADMIN, Ultrabans.DEFAULT_ADMIN);
            if (bcmsg.contains(Formatting.VICTIM)) bcmsg = bcmsg.replaceAll(Formatting.VICTIM, player.getName());
            player.sendMessage(Formatting.replaceAmpersand(bcmsg));
            return true;
        }
        Date date = new Date();
        date.setTime(tempTime * 1000);
        String dateStr = date.toString();
        player.sendMessage(ChatColor.GRAY + "You've been tempjailed for " + info.getReason());
        player.sendMessage(ChatColor.GRAY + "Remaining: " + ChatColor.RED + dateStr);
        return false;
    }

    private void ipCheck(Player player, String message, AsyncPlayerChatEvent event) {
        String mes = message;
        String[] content = {"\\,", "\\-", "\\_", "\\="};
        for (String aContent : content) {
            if (mes.contains(aContent)) mes = mes.replaceAll(aContent, ".");
        }
        String[] ipcheck = mes.split(" ");
        String mode = config.getString("Chat.IPCheck.Blocking");
        if (mode == null) mode = "";
        boolean valid = false;
        for (String anIpcheck : ipcheck) {
            if (Formatting.validIP(anIpcheck.trim())) {
                if (mode.equalsIgnoreCase("%scramble%")) {
                    event.setMessage(mes.replaceAll(anIpcheck.trim(), ChatColor.MAGIC + "AAAAA"));
                } else if (mode.equalsIgnoreCase("%replace%")) {
                    event.setMessage(mes.replaceAll(anIpcheck.trim(), plugin.getServer().getIp()));
                } else {
                    event.setMessage(mes.replaceAll(anIpcheck.trim(), mode));
                }
                valid = true;
            }
        }
        String result = config.getString("Chat.IPCheck.Result", "ban");
        String reason = config.getString("Chat.IPCheck.Reason", "Advertising");
        if (valid && result != null) {
            if (result.equalsIgnoreCase("ban") || result.equalsIgnoreCase("kick") || result.equalsIgnoreCase("ipban") || result.equalsIgnoreCase("jail") || result.equalsIgnoreCase("warn")) {
                String fakecmd;
                if (config.getBoolean("Chat.IPCheck.Silent", false)) {
                    fakecmd = result + " " + player.getName() + " " + "-s" + " " + reason;
                } else {
                    fakecmd = result + " " + player.getName() + " " + reason;
                }
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), fakecmd);
            }
        }
    }

    private void spamCheck(Player player, String message, AsyncPlayerChatEvent event) {
        if (!message.equalsIgnoreCase(spamcheck)) {
            spamcheck = event.getMessage();
            spamCount = 0;
        } else {
            event.setCancelled(true);
            spamCount++;
        }
        String result = config.getString("Chat.SpamCheck.Result", "kick");
        String reason = config.getString("Chat.SpamCheck.Reason", "Spam");
        if (config.getInt("Chat.SpamCheck.Counter") < spamCount && result != null) {
            if (result.equalsIgnoreCase("ban") || result.equalsIgnoreCase("kick") || result.equalsIgnoreCase("ipban") || result.equalsIgnoreCase("jail") || result.equalsIgnoreCase("warn")) {
                String fakecmd;
                if (config.getBoolean("Chat.SpamCheck.Silent", false)) {
                    fakecmd = result + " " + player.getName() + " " + "-s" + " " + reason;
                } else {
                    fakecmd = result + " " + player.getName() + " " + reason;
                }
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), fakecmd);
            }
        }

    }

    private void swearCheck(Player player, String message, AsyncPlayerChatEvent event) {
        String mes = message;
        String[] string = config.getString("Chat.SwearCensor.Words").split(" ");
        String mode = config.getString("Chat.SwearCensor.Blocking");
        if (mode == null) mode = "";
        boolean valid = false;
        for (String aString : string) {
            if (Pattern.compile(Pattern.quote(aString.trim()), Pattern.CASE_INSENSITIVE).matcher(mes).find()) {
                if (mode.equalsIgnoreCase("%scramble%")) {
                    mes = mes.replaceAll(aString.trim(), ChatColor.MAGIC + "AAAAA");
                } else {
                    mes = mes.replaceAll(aString.trim(), mode);
                }
                valid = true;
            }
        }
        event.setMessage(mes);
        String result = config.getString("Chat.SwearCensor.Result", "mute");
        String reason = config.getString("Chat.SwearCensor.Reason", "Language");
        if (valid && result != null) {
            if (result.equalsIgnoreCase("ban") || result.equalsIgnoreCase("kick") || result.equalsIgnoreCase("ipban") || result.equalsIgnoreCase("jail") || result.equalsIgnoreCase("warn") || result.equalsIgnoreCase("mute")) {
                String fakecmd;
                if (config.getBoolean("Chat.SwearCensor.Silent", false)) {
                    fakecmd = result + " " + player.getName() + " " + "-s" + " " + reason;
                } else {
                    fakecmd = result + " " + player.getName() + " " + reason;
                }
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), fakecmd);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (plugin.cache.containsKey(player.getName().toLowerCase())) {
            List<BanInfo> list = plugin.cache.get(player.getName().toLowerCase());
            for (BanInfo info : list) {
                if (info.getType() == BanType.TEMPJAIL || info.getType() == BanType.JAIL) {
                    if (tempjailCheck(player, info))
                        return;
                    event.setRespawnLocation(plugin.jail.getJail("jail"));
                }
            }
        }
    }
}
